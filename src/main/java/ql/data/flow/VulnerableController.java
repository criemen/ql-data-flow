package ql.data.flow;

import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VulnerableController {
	public static String vuln(String input) {
		return Paths.get(input).toString();
	}

	public static String vulnIfX0(String input, int x) {
		if (x == 0) {
			return Paths.get(input).toString();
		} else {
			return "nothing";
		}
	}
	
	public static String vulnIfFalse(String input, boolean cond) {
		if (!cond) {
			return Paths.get(input).toString();
		} else {
			return "nothing";
		}
	}

	@GetMapping("/vulnerable")
	public String vulnerable(@RequestParam(name = "name", required = false, defaultValue = "/etc/passwd") String path) {
		// should be enough to trigger the taint analysis
		return vuln(path);
	}

	@GetMapping("/deadCode")
	public String deadCode(@RequestParam(name = "name", required = false, defaultValue = "/etc/passwd") String path) {
		if (true == true) {
			return "dead code";
		}
		return vuln(path);
	}

	@GetMapping("/constant propagation - local")
	public String constantPropagationLocal(
			@RequestParam(name = "name", required = false, defaultValue = "/etc/passwd") String path) {
		int x = 0;
		if (x == 0) {
			return "dead code";
		}
		return vuln(path);
	}

	@GetMapping("/branchTracking")
	public String branchTracking(
			@RequestParam(name = "name", required = false, defaultValue = "/etc/passwd") String path, String what) {
		// flow needs to discover that in this branch, conditional always is true, so vulnerability does not hold
		if(what == "bla") {
			return vulnIfFalse(path, what == "bla");
		}
		return "nothing";
	}
}
