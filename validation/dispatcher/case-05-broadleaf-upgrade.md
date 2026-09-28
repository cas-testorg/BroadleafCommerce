# Workflow Dispatch — Case 05: Broadleaf Renovate Upgrade

## Request Summary

Renovate has applied a major dependency upgrade in the Broadleaf Commerce Spring monorepo, and CI now fails. The issue asks an agent to break that work into tickets and fix everything.

## Incident Disposition

`INVALID_AUTOMATED_CHANGE`

An automated dependency update is incompatible with the current application: CI fails after the Renovate major upgrade. The request does not describe a pre-existing application defect, expected behavior, or a contradictory failure report.

## Next-Work Classification

`CODE_MODERNIZATION`

## Recommended Playbook

Code Modernization — assess the current stack, dependency coupling, and upgrade risks, then define a phased compatibility approach before implementation.

## Recommended Skill

`code-modernization`

## Execution Variant

`NOT_APPLICABLE`

`CODE_MODERNIZATION` supports `STANDARD` and `VIBE_MODERNIZATION`, but the execution model is not established. The request does not show that behavioral parity, independent verification, human decision gates, and an intentional agent-led execution model are all in place. Vibe Modernization also requires the modernization objective and scope to be understood before implementation. Select a variant only after the assessment, with human review if the execution model is still unclear.

## Downstream Workflow Extension

`CONSTRAINT_TICKET_DECOMPOSITION`

The original request is not sufficiently bounded for decomposition. A major upgrade across a Spring monorepo, described only as “fix everything,” is the unbounded incident pattern. Decomposition is appropriate only after Code Modernization produces an evidence-backed assessment.

Before decomposition begins, the primary workflow must establish:

- The upgraded dependencies and target versions, and whether that upgrade should be kept.
- The CI failure modes and the compatibility conditions each failure represents.
- Which failures are separable outcomes (API compatibility, dependency convergence, configuration, behavioral parity) rather than one localized break.
- Affected modules and why each constraint exists, with enough evidence to state a checkable finished-system property.
- Sequencing and validation expectations for those constraints.

Do not turn “break the work into tickets” or “fix everything” into constraint tickets. Those are activities, not outcomes.

## Confidence

`MEDIUM`

The incident shape matches an incompatible automated major upgrade, and that work is broader than a localized defect. The specific dependency, diff, and CI evidence are still missing, so Bug Resolution or Automation Configuration could become the better primary workflow if the failure is narrow or the correct response is to reject the automation.

## Rationale

The failing change is a Renovate major dependency upgrade, not an established application defect and not a request to reconfigure CI or Renovate. Bug Resolution excludes broad framework or dependency migrations and excludes cases where the automated change itself is invalid. Automation Configuration would apply if the primary need were to correct dependency automation or the pipeline. Code Modernization is the workflow for assessing a broader dependency upgrade and its compatibility risks before implementation.

The issue’s request to split the work and fix everything does not make Constraint-Ticket Decomposition the primary workflow. That extension runs only after a primary workflow has bounded the problem. A monorepo major-upgrade assessment is likely to yield multiple compatibility constraints, so decomposition is the downstream extension, not the next action.

## Evidence

- The request states that Renovate performed a major dependency upgrade and that CI now fails.
- The requested action is unbounded: break the work into tickets and fix everything. No dependency name, version, diff, failing job, or reproduction is provided.
- `INVALID_AUTOMATED_CHANGE` covers an automated dependency update that is inappropriate or incompatible.
- Agentic Bug Resolution is not for a broad migration or for an invalid automated change with no established application defect.
- Code Modernization is for upgrade and migration assessment when architectural or dependency impact must be understood first, and it excludes a single isolated dependency update with no broader impact. Isolation is not established here.
- Constraint-ticket rules require a completed primary workflow and forbid decomposing an unbounded statement such as upgrading a framework and making everything work. A broad CI or dependency-upgrade incident is routed to a primary workflow first.
- Vibe Modernization’s entry criteria are not satisfied by this request.
- CoreStory project 603 was not queried. The request’s shape is enough to choose the primary workflow and to refuse immediate decomposition. Repository contents would not make the current incident bounded.

## Alternatives Considered

- `BUG_RESOLUTION` (`agentic-bug-resolution`): a CI failure can be a repairable defect, but this request is a major upgrade across a monorepo with no localized root cause. The bug workflow redirects broad migrations and invalid automated changes elsewhere.
- `AUTOMATION_CONFIGURATION`: appropriate if the upgrade should be reverted or Renovate/CI policy should change. The issue asks to fix the upgrade’s application impact, and no automation-configuration defect is described.
- Direct `CONSTRAINT_TICKET_DECOMPOSITION`: the issue asks for tickets, but the input is an uninvestigated dependency-upgrade incident. Decomposition is downstream only.
- `HUMAN_TRIAGE_REQUIRED`: competing primaries exist until CI and diff evidence arrive, but the best current fit is still Code Modernization. Confidence is medium, not low.

## Required Inputs or Missing Information

- Which dependencies Renovate changed, including from/to versions, and the pull request or diff.
- CI logs: failing jobs, modules, and whether failures are compile, test, or configuration errors.
- Whether the upgrade is accepted work or should be rejected as an invalid automation result.
- Whether the impact is one isolated dependency with a single correction, or a broader Spring/framework compatibility change.
- Any approved modernization target, behavioral baseline, or verification gate.

## Recommended Next Action

Run the Code Modernization assessment on the Renovate upgrade and the failing CI signal. Do not decompose tickets and do not change application or automation code until that assessment bounds the compatibility work. After the assessment, decompose only the evidence-backed constraints, or reroute if the evidence shows a localized defect or an automation-configuration fix.

## Explicit Answers

1. **Is the original request sufficiently bounded for constraint-ticket decomposition?** No. It is an uninvestigated major-upgrade incident whose only implementation instruction is to break the work into tickets and fix everything.
2. **What must the primary workflow establish before decomposition can begin?** Code Modernization must identify the accepted upgrade scope, the distinct CI compatibility conditions, why each condition exists, the affected areas, and how each resulting outcome can be checked. Decomposition starts from that assessment, not from the original issue text.
3. **What evidence would change the routing decision?** A single localized failure with a clear correction would move the primary workflow to Bug Resolution. Evidence that the correct response is to revert the bump or change Renovate/CI policy would move it to Automation Configuration. A completed assessment that finds one independently actionable correction would drop the downstream decomposition extension. Established behavioral parity, verification, and an intentional agent-led execution model would allow a `VIBE_MODERNIZATION` variant after the primary classification. Contradictory or still-unbounded failure evidence would lower confidence and route to human triage.
4. **Did you invoke any downstream workflow or make any modifications?** No. No downstream skill was executed, CoreStory project 603 was not queried, and no application code, tickets, or repository state were changed. This file is the dispatch record requested for independent review.
