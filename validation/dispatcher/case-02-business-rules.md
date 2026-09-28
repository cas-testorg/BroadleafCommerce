# Workflow Dispatch — Case 02: Account Balance Retrieval Business Rules

## Request Summary

Establish the business rules that Bank of Z actually enforces for Account Balance Retrieval in CoreStory project 733. Existing documentation may be incomplete. The work is to identify the rules the application enforces, substantiate them with source evidence, and record contradictions or behavior that cannot be established. A validated business-rule inventory does not exist yet. Modernization specifications and behavioral tests are later uses of that inventory.

## Incident Disposition

`NOT_APPLICABLE`

The request does not describe an incident, failure, or observed incorrect behavior. It asks what rules the application currently enforces.

## Next-Work Classification

`BUSINESS_RULES_EXTRACTION`

## Recommended Playbook

Business Rules Extraction — build an evidence-backed inventory of the rules a bounded capability enforces, trace each rule to source artifacts, and mark rules that are contradicted or cannot be established.

## Recommended Skill

`business-rules-extraction`

The immediate objective is the skill’s purpose: determine what rules an existing application enforces for a named capability, substantiate them against implementation evidence, and separate supported rules from contradictions and insufficient evidence. Account Balance Retrieval is a bounded workflow. Project 733 is the stated CoreStory context. Documentation is a hypothesis to validate, not the authority for the rules.

## Execution Variant

`NOT_APPLICABLE`

Business Rules Extraction does not define an execution variant. `STANDARD` and `VIBE_MODERNIZATION` apply only after a primary workflow that supports them, and `VIBE_MODERNIZATION` requires `CODE_MODERNIZATION`.

## Downstream Workflow Extension

`NOT_APPLICABLE`

Modernization specifications and behavioral tests are possible later consumers of a validated inventory. They are not the current objective, and they are not a dispatcher downstream extension. The only supported extension is `CONSTRAINT_TICKET_DECOMPOSITION`. Its entry criteria are not met: there is no implementation change to bound, and the inventory that would explain why any later constraint exists has not been produced. Do not decompose this request into tickets.

## Confidence

`HIGH`

The request states the classification boundary directly: extract and substantiate the rules the application enforces, and do not treat modernization or behavioral tests as current work because no validated inventory exists. No competing primary workflow fits that objective.

## Rationale

`BUSINESS_RULES_EXTRACTION` applies when the primary objective is to determine what rules the existing application enforces, substantiate specifications against implementation evidence, and build a behavioral contract before modernization or testing. That is this request.

Behavioral Test Coverage is not appropriate at this stage. That workflow requires supported rules, acceptance criteria, or an equivalent specification, preferably a prior extraction result with evidence states. Rules that are contradicted or insufficiently evidenced must be resolved through extraction, focused investigation, or human review before they are treated as expected behavior. The request says the inventory does not exist and that documentation may be incomplete, so there is no behavioral contract to turn into tests.

Code Modernization is later work. It assesses readiness and a phased strategy after the behavior to preserve or change is known. This request stops at establishing that behavior.

## Evidence

- The capability is bounded: Account Balance Retrieval in Bank of Z, CoreStory project 733.
- The required outcome is the rules the application enforces, source substantiation, and identification of contradictions or behavior that cannot be established.
- Existing documentation is explicitly unreliable as a finished inventory.
- Modernization specifications and behavioral tests are described as eventual uses, and the request states that a validated business-rule inventory does not yet exist.
- Classification rules send this objective to Business Rules Extraction and forbid routing insufficiently evidenced rules into Behavioral Test Coverage.
- `business-rules-extraction` requires a CoreStory project and a bounded capability or workflow. Both are named. It treats existing documentation as a hypothesis and classifies contradicted or insufficient evidence rather than freezing it as expected behavior.
- `behavioral-test-coverage` refuses contradicted or insufficiently evidenced rules and prefers a prior BR inventory.
- CoreStory was not queried. The request text is sufficient to select the primary workflow. Confirming whether project 733 contains this capability would not change the classification.

## Alternatives Considered

- `BEHAVIORAL_TEST_COVERAGE` (`behavioral-test-coverage`): the eventual test use is stated, but there is no validated rule inventory to cover. Generating tests now would treat unresolved rules as expected behavior.
- `CODE_MODERNIZATION` (`code-modernization`): modernization specifications are a later consumer. The request does not ask for a modernization assessment or implementation plan.
- `CODEBASE_ASSESSMENT`: the scope is one capability, not an unfamiliar application as a whole.
- `FEATURE_GAP_ANALYSIS`: no new or changed capability is requested.
- `BUG_RESOLUTION`: no defect or incorrect balance behavior is reported.
- `CONSTRAINT_TICKET_DECOMPOSITION` as the primary workflow: there is no bounded implementation outcome to ticket.
- `HUMAN_TRIAGE_REQUIRED`: the primary workflow is determined. Missing project contents affect execution of extraction, not the routing choice.

## Required Inputs or Missing Information

None are required to select the workflow. Project 733 and Account Balance Retrieval satisfy the skill’s minimum inputs.

These items are unresolved and would be established inside extraction, or would change the recommendation if they contradict the request:

- Whether project 733 is available in CoreStory and actually contains Account Balance Retrieval.
- Entry point, actor, outcome, and known variants (channel, product, tenant, environment) that bound the capability.
- Existing documentation, user stories, or SME assertions to treat as hypotheses.
- Whether any rule is already known to be wrong in production. A reported defect would move the primary workflow to Bug Resolution.
- A completed inventory with supported rules would make Behavioral Test Coverage appropriate as a later workflow.
- An approved modernization objective, after that inventory exists, would make Code Modernization appropriate as a later workflow. Constraint-ticket decomposition would become appropriate only after a primary workflow has produced discrete, evidence-backed outcomes.

## Recommended Next Action

Run Business Rules Extraction for Account Balance Retrieval in CoreStory project 733. Use existing documentation only as a hypothesis. Catalog each rule with source evidence and an evidence state, including contradictions and behavior that cannot be established. Do not generate behavioral tests, modernization specifications, or constraint tickets from this request.

## Explicit Answers

1. **Primary next-work classification:** `BUSINESS_RULES_EXTRACTION`.
2. **Recommended skill:** `business-rules-extraction`, because the objective is an evidence-backed inventory of rules a bounded capability already enforces, including contradictions and gaps.
3. **Immediate objective versus later work:** The immediate objective is the inventory. Modernization specifications, behavioral tests, and any constraint tickets are later work and are not selected here.
4. **Is Behavioral Test Coverage appropriate at this stage?** No. It requires a supported behavioral contract. The request states that contract does not exist, and contradicted or insufficiently evidenced rules must not be turned into normative tests.
5. **Missing evidence and conditions that would change the recommendation:** Routing does not depend on further application evidence. A reported defect would route to Bug Resolution. An application-wide discovery request would route to Codebase Assessment. A request for new balance-retrieval behavior would route to Feature Gap Analysis. A validated inventory would allow Behavioral Test Coverage later. An approved modernization objective after that inventory would allow Code Modernization later.
6. **Did you invoke any downstream workflow or make any modifications?** No. No downstream skill was executed, CoreStory was not queried, and no application code, tickets, or other repository state were changed. This file is the dispatch record requested for independent review.
