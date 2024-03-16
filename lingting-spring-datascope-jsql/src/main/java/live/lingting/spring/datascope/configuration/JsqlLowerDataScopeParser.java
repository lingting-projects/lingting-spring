package live.lingting.spring.datascope.configuration;

import live.lingting.framework.datascope.JsqlDataScope;
import live.lingting.framework.datascope.holder.DataScopeHolder;
import live.lingting.framework.datascope.holder.DataScopeMatchNumHolder;
import live.lingting.framework.datascope.parser.DataScopeParser;
import live.lingting.framework.datascope.util.SqlParseUtils;
import live.lingting.framework.util.CollectionUtils;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lingting 2024-03-16 17:00
 */
@SuppressWarnings("all")
public class JsqlLowerDataScopeParser extends DataScopeParser {

	/**
	 * insert 类型SQL处理
	 * @param insert jsqlparser Statement Insert
	 */
	@Override
	protected void insert(Insert insert, int index, String sql) {
		// insert 暂时不处理
	}

	/**
	 * delete 类型SQL处理
	 * @param delete jsqlparser Statement Delete
	 */
	@Override
	protected void delete(Delete delete, int index, String sql) {
		Expression expression = this.injectExpression(delete.getWhere(), delete.getTable());
		delete.setWhere(expression);
	}

	/**
	 * update 类型SQL处理
	 * @param update jsqlparser Statement Update
	 */
	@Override
	protected void update(Update update, int index, String sql) {
		Expression expression = this.injectExpression(update.getWhere(), update.getTable());
		update.setWhere(expression);
	}

	/**
	 * select 类型SQL处理
	 * @param select jsqlparser Statement Select
	 */
	@Override
	protected void select(Select select, int index, String sql) {
		processSelectBody(select.getSelectBody());
		List<WithItem> withItemsList = select.getWithItemsList();
		if (!CollectionUtils.isEmpty(withItemsList)) {
			withItemsList.forEach(this::processSelectBody);
		}
	}

	protected void processSelectBody(SelectBody selectBody) {
		if (selectBody == null) {
			return;
		}
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody);
		}
		else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			processSelectBody(withItem.getSubSelect().getSelectBody());
		}
		else {
			SetOperationList operationList = (SetOperationList) selectBody;
			List<SelectBody> selectBodys = operationList.getSelects();
			if (!CollectionUtils.isEmpty(selectBodys)) {
				selectBodys.forEach(this::processSelectBody);
			}
		}
	}

	/**
	 * 处理 PlainSelect
	 */
	protected void processPlainSelect(PlainSelect plainSelect) {
		// #3087 github
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		if (!CollectionUtils.isEmpty(selectItems)) {
			selectItems.forEach(this::processSelectItem);
		}

		// 处理 where 中的子查询
		Expression where = plainSelect.getWhere();
		processWhereSubSelect(where);

		// 处理 fromItem
		FromItem fromItem = plainSelect.getFromItem();
		List<Table> list = processFromItem(fromItem);
		List<Table> mainTables = new ArrayList<>(list);

		// 处理 join
		List<Join> joins = plainSelect.getJoins();
		if (!CollectionUtils.isEmpty(joins)) {
			mainTables = processJoins(mainTables, joins);
		}

		// 当有 mainTable 时，进行 where 条件追加
		if (!CollectionUtils.isEmpty(mainTables)) {
			plainSelect.setWhere(injectExpression(where, mainTables));
		}
	}

	private List<Table> processFromItem(FromItem fromItem) {
		// 处理括号括起来的表达式
		while (fromItem instanceof ParenthesisFromItem) {
			fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
		}

		List<Table> mainTables = new ArrayList<>();
		// 无 join 时的处理逻辑
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			mainTables.add(fromTable);
		}
		else if (fromItem instanceof SubJoin) {
			// SubJoin 类型则还需要添加上 where 条件
			List<Table> tables = processSubJoin((SubJoin) fromItem);
			mainTables.addAll(tables);
		}
		else {
			// 处理下 fromItem
			processOtherFromItem(fromItem);
		}
		return mainTables;
	}

	/**
	 * 处理where条件内的子查询
	 * <p>
	 * 支持如下: 1. in 2. = 3. > 4. < 5. >= 6. <= 7. <> 8. EXISTS 9. NOT EXISTS
	 * <p>
	 * 前提条件: 1. 子查询必须放在小括号中 2. 子查询一般放在比较操作符的右边
	 * @param where where 条件
	 */
	protected void processWhereSubSelect(Expression where) {
		if (where == null) {
			return;
		}
		if (where instanceof FromItem) {
			processOtherFromItem((FromItem) where);
			return;
		}
		if (where.toString().indexOf("SELECT") > 0) {
			// 有子查询
			if (where instanceof BinaryExpression) {
				// 比较符号 , and , or , 等等
				BinaryExpression expression = (BinaryExpression) where;
				processWhereSubSelect(expression.getLeftExpression());
				processWhereSubSelect(expression.getRightExpression());
			}
			else if (where instanceof InExpression) {
				// in
				InExpression expression = (InExpression) where;
				Expression inExpression = expression.getRightExpression();
				if (inExpression instanceof SubSelect) {
					processSelectBody(((SubSelect) inExpression).getSelectBody());
				}
			}
			else if (where instanceof ExistsExpression) {
				// exists
				ExistsExpression expression = (ExistsExpression) where;
				processWhereSubSelect(expression.getRightExpression());
			}
			else if (where instanceof NotExpression) {
				// not exists
				NotExpression expression = (NotExpression) where;
				processWhereSubSelect(expression.getExpression());
			}
			else if (where instanceof Parenthesis) {
				Parenthesis expression = (Parenthesis) where;
				processWhereSubSelect(expression.getExpression());
			}
		}
	}

	protected void processSelectItem(SelectItem selectItem) {
		if (selectItem instanceof SelectExpressionItem) {
			SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
			if (selectExpressionItem.getExpression() instanceof SubSelect) {
				processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
			}
			else if (selectExpressionItem.getExpression() instanceof Function) {
				processFunction((Function) selectExpressionItem.getExpression());
			}
		}
	}

	/**
	 * 处理函数
	 * <p>
	 * 支持: 1. select fun(args..) 2. select fun1(fun2(args..),args..)
	 * <p>
	 * <p>
	 * fixed gitee pulls/141
	 * </p>
	 * @param function
	 */
	protected void processFunction(Function function) {
		ExpressionList parameters = function.getParameters();
		if (parameters != null) {
			parameters.getExpressions().forEach(expression -> {
				if (expression instanceof SubSelect) {
					processSelectBody(((SubSelect) expression).getSelectBody());
				}
				else if (expression instanceof Function) {
					processFunction((Function) expression);
				}
			});
		}
	}

	/**
	 * 处理子查询等
	 */
	protected void processOtherFromItem(FromItem fromItem) {
		// 去除括号
		while (fromItem instanceof ParenthesisFromItem) {
			fromItem = ((ParenthesisFromItem) fromItem).getFromItem();
		}

		if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody());
			}
		}
		else if (fromItem instanceof ValuesList) {
			log.debug("Perform a subquery, if you do not give us feedback");
		}
		else if (fromItem instanceof LateralSubSelect) {
			LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
			if (lateralSubSelect.getSubSelect() != null) {
				SubSelect subSelect = lateralSubSelect.getSubSelect();
				if (subSelect.getSelectBody() != null) {
					processSelectBody(subSelect.getSelectBody());
				}
			}
		}
	}

	/**
	 * 处理 sub join
	 * @param subJoin subJoin
	 * @return Table subJoin 中的主表
	 */
	private List<Table> processSubJoin(SubJoin subJoin) {
		List<Table> mainTables = new ArrayList<>();
		if (subJoin.getJoinList() != null) {
			List<Table> list = processFromItem(subJoin.getLeft());
			mainTables.addAll(list);
			mainTables = processJoins(mainTables, subJoin.getJoinList());
		}
		return mainTables;
	}

	/**
	 * 处理 joins
	 * @param mainTables 可以为 null
	 * @param joins join 集合
	 * @return List
	 * <Table>
	 * 右连接查询的 Table 列表
	 */
	@SuppressWarnings("java:S6541")
	protected List<Table> processJoins(List<Table> mainTables, List<Join> joins) {
		if (mainTables == null) {
			mainTables = new ArrayList<>();
		}

		// join 表达式中最终的主表
		Table mainTable = null;
		// 当前 join 的左表
		Table leftTable = null;
		if (mainTables.size() == 1) {
			mainTable = mainTables.get(0);
			leftTable = mainTable;
		}

		// 对于 on 表达式写在最后的 join，需要记录下前面多个 on 的表名
		Deque<List<Table>> onTableDeque = new LinkedList<>();
		for (Join join : joins) {
			// 处理 on 表达式
			FromItem joinItem = join.getRightItem();

			// 获取当前 join 的表，subJoint 可以看作是一张表
			List<Table> joinTables = null;
			if (joinItem instanceof Table) {
				joinTables = new ArrayList<>();
				joinTables.add((Table) joinItem);
			}
			else if (joinItem instanceof SubJoin) {
				joinTables = processSubJoin((SubJoin) joinItem);
			}

			if (joinTables != null) {

				// 如果是隐式内连接
				if (join.isSimple()) {
					mainTables.addAll(joinTables);
					continue;
				}

				// 当前表是否忽略
				Table joinTable = joinTables.get(0);

				List<Table> onTables = null;
				// 如果不要忽略，且是右连接，则记录下当前表
				if (join.isRight()) {
					mainTable = joinTable;
					if (leftTable != null) {
						onTables = Collections.singletonList(leftTable);
					}
				}
				else if (join.isLeft()) {
					onTables = Collections.singletonList(joinTable);
				}
				// JOIN 等同于 INNER JOIN
				else if (join.isInner() || join.getASTNode().jjtGetFirstToken().toString().equalsIgnoreCase("JOIN")) {
					if (mainTable == null) {
						onTables = Collections.singletonList(joinTable);
					}
					else {
						onTables = Arrays.asList(mainTable, joinTable);
					}
					mainTable = null;
				}

				mainTables = new ArrayList<>();
				if (mainTable != null) {
					mainTables.add(mainTable);
				}

				// 获取 join 尾缀的 on 表达式列表
				Collection<Expression> originOnExpressions = join.getOnExpressions();
				// 正常 join on 表达式只有一个，立刻处理
				if (originOnExpressions.size() == 1 && onTables != null) {
					List<Expression> onExpressions = new LinkedList<>();
					onExpressions.add(injectExpression(originOnExpressions.iterator().next(), onTables));
					join.setOnExpressions(onExpressions);
					leftTable = joinTable;
					continue;
				}
				// 表名压栈，忽略的表压入 null，以便后续不处理
				onTableDeque.push(onTables);
				// 尾缀多个 on 表达式的时候统一处理
				if (originOnExpressions.size() > 1) {
					Collection<Expression> onExpressions = new LinkedList<>();
					for (Expression originOnExpression : originOnExpressions) {
						List<Table> currentTableList = onTableDeque.poll();
						if (CollectionUtils.isEmpty(currentTableList)) {
							onExpressions.add(originOnExpression);
						}
						else {
							onExpressions.add(injectExpression(originOnExpression, currentTableList));
						}
					}
					join.setOnExpressions(onExpressions);
				}
				leftTable = joinTable;
			}
			else {
				processOtherFromItem(joinItem);
				leftTable = null;
			}

		}

		return mainTables;
	}

	/**
	 * 根据 DataScope ，将数据过滤的表达式注入原本的 where/or 条件
	 * @param currentExpression Expression where/or
	 * @param table 表信息
	 * @return 修改后的 where/or 条件
	 */
	private Expression injectExpression(Expression currentExpression, Table table) {
		return injectExpression(currentExpression, Collections.singletonList(table));
	}

	/**
	 * 根据 DataScope ，将数据过滤的表达式注入原本的 where/or 条件
	 * @param currentExpression Expression where/or
	 * @param tables 表信息
	 * @return 修改后的 where/or 条件
	 */
	private Expression injectExpression(Expression currentExpression, List<Table> tables) {
		// 没有表需要处理直接返回
		if (CollectionUtils.isEmpty(tables)) {
			return currentExpression;
		}

		List<Expression> dataFilterExpressions = new ArrayList<>(tables.size());
		for (Table table : tables) {
			// 获取表名
			String tableName = SqlParseUtils.getTableName(table.getName());

			// 进行 dataScope 的表名匹配
			List<JsqlDataScope> matchDataScopes = DataScopeHolder.peek()
				.stream()
				.filter(x -> x.includes(tableName))
				.collect(Collectors.toList());

			if (CollectionUtils.isEmpty(matchDataScopes)) {
				continue;
			}

			// 匹配则计数
			DataScopeMatchNumHolder.incrementMatchNumIfPresent();

			// 获取到数据权限过滤的表达式
			matchDataScopes.stream()
				.map(x -> x.getExpression(tableName, table.getAlias()))
				.filter(Objects::nonNull)
				.reduce(AndExpression::new)
				.ifPresent(dataFilterExpressions::add);
		}

		if (dataFilterExpressions.isEmpty()) {
			return currentExpression;
		}

		// 注入的表达式
		Expression injectExpression = dataFilterExpressions.get(0);
		// 如果有多个，则用 and 连接
		if (dataFilterExpressions.size() > 1) {
			for (int i = 1; i < dataFilterExpressions.size(); i++) {
				injectExpression = new AndExpression(injectExpression, dataFilterExpressions.get(i));
			}
		}

		if (currentExpression == null) {
			return injectExpression;
		}
		if (injectExpression == null) {
			return currentExpression;
		}
		if (currentExpression instanceof OrExpression) {
			return new AndExpression(new Parenthesis(currentExpression), injectExpression);
		}
		else {
			return new AndExpression(currentExpression, injectExpression);
		}
	}

}
