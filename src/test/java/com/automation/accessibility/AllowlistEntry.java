package com.automation.accessibility;

/**
 * A known, accepted violation that should not fail the build.
 *
 * @param ruleId the axe rule being accepted, e.g. {@code color-contrast}
 * @param page the page the acceptance applies to
 * @param reason why the violation is accepted, e.g. a ticket reference
 */
public record AllowlistEntry(String ruleId, String page, String reason) {}
