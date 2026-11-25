package com.automation.accessibility;

import java.util.List;

/**
 * A single accessibility violation reported by axe-core.
 *
 * @param ruleId the axe rule that failed, e.g. {@code color-contrast}
 * @param impact the severity axe assigned to the violation
 * @param description what the rule checks
 * @param helpUrl link to Deque's guidance for fixing the violation
 * @param selectors CSS selectors of the affected elements
 */
public record Violation(
    String ruleId, Impact impact, String description, String helpUrl, List<String> selectors) {}
