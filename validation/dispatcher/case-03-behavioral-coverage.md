# Workflow Dispatch — Case 03: Account Balance Retrieval Behavioral Coverage

## Request Summary

Bank of Z Account Balance Retrieval has already completed Business Rules Extraction. The reviewed inventory contains supported BR-XXX rules with source evidence and established expected outcomes. Contradictory or insufficiently evidenced rules are excluded from the approved behavioral baseline. The engineering team wants to see which supported rules already have tests, identify missing behavioral cases, and develop tests in the application's existing testing framework. The immediate objective is behavioral coverage of the existing capability.

## Incident Disposition

`NOT_APPLICABLE`

The request does not describe an incident, failure, or observed incorrect behavior. It asks for test coverage of an already approved behavioral baseline.

## Next-Work Classification

`BEHAVIORAL_TEST_COVERAGE`

## Recommended Playbook

Behavioral Test Coverage — map an already established behavioral contract to existing and missing tests, design the missing cases, and generate them in the application's existing test framework.

## Recommended Skill

`behavioral-test-coverage`

This skill applies because expected behavior is already established and the primary objective is to determine whether those rules are tested, design the missing behavioral cases, and generate tests in the existing framework. The skill's entry condition is a Business Rules Extraction inventory whose supported rules need executable coverage. Account Balance Retrieval is a bounded, already-implemented capability. The request states that the reviewed inventory exists and that contradicted or insufficiently evidenced rules have already been excluded.

## Execution Variant

`NOT_APPLICABLE`

Behavioral Test Coverage does not define an execution variant. `STANDARD` and `VIBE_MODERNIZATION` apply only after a primary workflow that supports them, and `VIBE_MODERNIZATION` requires `CODE_MODERNIZATION`.

## Downstream Workflow Extension

`NOT_APPLICABLE`

Mapping coverage, designing missing cases, and generating tests are steps inside Behavioral Test Coverage. They are not a later workflow. The only supported extension is `CONSTRAINT_TICKET_DECOMPOSITION`. Its entry criteria are not met: this is one bounded capability with a clear validation path, and the work is not a set of implementation constraints that must be ticketed before the coverage workflow runs.

## Confidence

`HIGH`

The request states the classification boundary directly: a reviewed, supported BR-XXX inventory already exists, excluded rules are out of the baseline, and the work is coverage of the existing capability. Discovering new rules, fixing a defect, and modernizing the application are explicitly out of scope. No competing primary workflow fits that objective.

## Rationale

`BEHAVIORAL_TEST_COVERAGE` applies when expected behavior is already sufficiently established and the primary objective is to determine whether those rules are tested, design missing behavioral cases, or generate tests in the existing framework. That is this request.

Business Rules Extraction does not apply. That workflow determines what rules the application enforces, substantiates them with source evidence, and separates supported rules from contradictions and insufficient evidence. The request states that this work is already complete and reviewed. Re-running extraction would rediscover rules rather than cover the approved baseline.

Behavioral Verification does not apply. Behavioral Test Coverage reserves that workflow for comparing a legacy implementation and a modernized implementation for parity. This request does not describe a modernization, a second implementation, or a parity comparison. It asks whether the current capability's supported rules have behavioral tests.

Bug Resolution, Feature Gap Analysis, Code Modernization, and Codebase Assessment do not apply. No defect is reported, no new capability is requested, and the scope is one already-understood capability rather than an application-wide assessment or a platform change.

## Evidence

- The capability is bounded and already implemented: Bank of Z Account Balance Retrieval.
- Business Rules Extraction is stated as complete and reviewed.
- The approved baseline is supported BR-XXX rules with source evidence and established expected outcomes.
- Contradictory and insufficiently evidenced rules are explicitly excluded from that baseline.
- The requested work is existing-test mapping, missing behavioral cases, and tests in the existing framework.
- The request excludes discovering new rules, fixing a known defect, and modernizing the application.
- Classification rules send a supported behavioral contract to Behavioral Test Coverage and forbid sending contradicted or insufficiently evidenced rules there as normative expected behavior.
- `behavioral-test-coverage` accepts a prior BR-XXX inventory and refuses contradicted or insufficiently evidenced rules.
- `behavioral-test-coverage` points Behavioral Verification at legacy-versus-modernized parity, which this request does not ask for.
- CoreStory was not queried. The request text is sufficient to select the primary workflow. Confirming the inventory contents would not change the classification.

## Alternatives Considered

- `BUSINESS_RULES_EXTRACTION` (`business-rules-extraction`): the inventory is an input, not the work to perform. The request says extraction is already complete and that new rule discovery is out of scope.
- Behavioral Verification: appropriate when a legacy and a modernized implementation must be compared for parity. This request has one existing capability and an approved baseline to cover with tests.
- `BUG_RESOLUTION`: no incorrect balance behavior or failing assertion is reported.
- `CODE_MODERNIZATION`: the request states modernization is not the objective.
- `FEATURE_GAP_ANALYSIS`: no new or changed capability is requested.
- `CODEBASE_ASSESSMENT`: the scope is one capability with an existing rule inventory, not an unfamiliar application.
- `CONSTRAINT_TICKET_DECOMPOSITION` as the primary workflow: coverage design and test generation belong to the selected playbook. There is no unbounded incident to ticket first.
- `HUMAN_TRIAGE_REQUIRED`: the primary workflow is determined. Missing inventory artifacts affect execution of coverage, not the routing choice.

## Required Inputs or Missing Information

None are required to select the workflow. A bounded capability and a reviewed inventory of supported rules with expected outcomes satisfy the routing entry criteria.

These items are required before test generation or execution, and would change the recommendation if they contradict the request:

- The completed, reviewed BR-XXX inventory itself, including each rule's identifier, source evidence, expected outcome, and evidence state. Normative tests may be generated only from `DIRECTLY_SUPPORTED` and `SUPPORTED_BY_CONFIGURATION` rules. `INFERRED` and `REQUIRES_RUNTIME_VALIDATION` rules may be recorded only as provisional specifications. `CONTRADICTED` and `INSUFFICIENT_EVIDENCE` rules stay excluded.
- The behavioral contract for each included rule: preconditions, trigger, observable outcome, error behavior, and variant or configuration context. Expected behavior must come from that contract, not from the implementation the tests will verify.
- The application's existing test framework, locations, fixtures, and assertion conventions, discovered after the contract is established.
- A coverage classification for each eligible rule (`COVERED`, `PARTIALLY_COVERED`, `UNCOVERED`, `BLOCKED`, or `PENDING_RULE_REVIEW`) before any test is written.
- Designed cases for uncovered or partially covered eligible rules before generation. Generation and execution follow that design.
- CoreStory project context for the capability, so the contract can be confirmed before local test code is inspected.
- A reported defect, a missing inventory, or an approved legacy-to-modern parity objective would move the work to Bug Resolution, Business Rules Extraction, or Behavioral Verification.

## Recommended Next Action

Run Behavioral Test Coverage for Bank of Z Account Balance Retrieval using the reviewed BR-XXX inventory as the behavioral contract. Confirm evidence states, map each eligible rule to existing tests, design missing cases, and only then generate tests in the existing framework. Do not extract new rules, compare a modernized implementation, fix defects, or create constraint tickets from this request.

## Explicit Answers

1. **Primary next-work classification:** `BEHAVIORAL_TEST_COVERAGE`.
2. **Recommended skill:** `behavioral-test-coverage`, because supported rules and expected outcomes already exist and the objective is to map them to tests, identify missing behavioral cases, and develop those tests in the existing framework.
3. **Immediate objective versus Business Rules Extraction and Behavioral Verification:** The immediate objective is coverage of an approved baseline. Business Rules Extraction discovers and evidences the rules; that work is already done. Behavioral Verification compares a legacy implementation with a modernized one for parity; this request does not modernize or compare two implementations.
4. **How the BR-XXX inventory should be used:** It is the behavioral contract. Include only supported rules with established outcomes. Keep excluded contradictory and insufficiently evidenced rules out of normative tests. For each included rule, record the observable contract, then classify existing coverage before designing or writing a test. Reference the BR-XXX identifier from the test. Do not treat the current implementation as the source of expected behavior.
5. **Evidence required before test generation or execution:** The reviewed inventory with evidence states, the observable contract for each included rule, existing test conventions, and a coverage map with designed cases for gaps. Generation and execution come after that evidence. `CONTRADICTED` and `INSUFFICIENT_EVIDENCE` rules are not eligible.
6. **Did you invoke any downstream workflow or make any modifications?** No. No downstream skill was executed, CoreStory was not queried, and no tests, tickets, application code, or other repository state were changed. This file is the dispatch record requested for independent review.
