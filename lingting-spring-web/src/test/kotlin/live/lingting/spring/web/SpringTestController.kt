package live.lingting.spring.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import live.lingting.framework.api.PaginationParams;
import live.lingting.framework.api.R;
import live.lingting.spring.web.scope.WebScope;
import live.lingting.spring.web.scope.WebScopeHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingting 2024-03-20 17:29
 */
@RestController
public class SpringTestController {

	public SpringTestController() {
	}

	@GetMapping("hello")
	public R<Void> hello() {
		return R.ok();
	}

	@GetMapping("pagination")
	public R<PaginationParams> pagination(PaginationParams pagination) {
		return R.ok(pagination);
	}

	@GetMapping("exception")
	public R<Void> exception() {
		throw new IllegalStateException();
	}

	@GetMapping("validation")
	public R<P> validation(@Valid P p) {
		return R.ok(p);
	}

	@GetMapping("scope")
	public R<WebScope> scope() {
		return R.ok(WebScopeHolder.get());
	}

	public static class P {

		public static final String MESSAGE = "name must not be empty.";

		@NotEmpty(message = MESSAGE)
		private String name;

		public @NotEmpty(message = MESSAGE) String getName() {
			return this.name;
		}

		public void setName(@NotEmpty(message = MESSAGE) String name) {
			this.name = name;
		}

	}

}
