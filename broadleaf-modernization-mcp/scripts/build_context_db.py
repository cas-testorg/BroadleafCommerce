import sqlite3
from pathlib import Path

DB_PATH = Path(__file__).resolve().parents[1] / "data" / "app_context.db"

DB_PATH.parent.mkdir(parents=True, exist_ok=True)

schema = """
DROP TABLE IF EXISTS components;
DROP TABLE IF EXISTS dependencies;
DROP TABLE IF EXISTS modernization_findings;
DROP TABLE IF EXISTS code_artifacts;

CREATE TABLE components (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    path TEXT,
    description TEXT
);

CREATE TABLE dependencies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    source TEXT NOT NULL,
    target TEXT NOT NULL,
    dependency_type TEXT,
    notes TEXT
);

CREATE TABLE modernization_findings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    area TEXT NOT NULL,
    finding TEXT NOT NULL,
    risk TEXT,
    recommendation TEXT
);

CREATE TABLE code_artifacts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    artifact_type TEXT NOT NULL,
    name TEXT NOT NULL,
    path TEXT,
    package TEXT,
    notes TEXT
);
"""

seed_data = {
    "components": [
        ("Broadleaf Commerce", "application", ".", "Large Java-based ecommerce platform with admin, core, common, and integration modules."),
        ("Admin", "module group", "admin", "Administrative and content management functionality."),
        ("Broadleaf Admin Module", "module", "admin/broadleaf-admin-module", "Core admin module."),
        ("Broadleaf Content Management Module", "module", "admin/broadleaf-contentmanagement-module", "Content management functionality."),
        ("Broadleaf Open Admin Platform", "module", "admin/broadleaf-open-admin-platform", "Admin platform foundation."),
        ("Common", "module", "common/src", "Shared/common code used across the platform."),
        ("Core", "module group", "core", "Core commerce framework and profile functionality."),
        ("Broadleaf Framework", "module", "core/broadleaf-framework", "Core commerce framework."),
        ("Broadleaf Framework Web", "module", "core/broadleaf-framework-web", "Web-facing framework functionality."),
        ("Broadleaf Profile", "module", "core/broadleaf-profile", "Customer/profile domain functionality."),
        ("Broadleaf Profile Web", "module", "core/broadleaf-profile-web", "Web-facing profile functionality."),
        ("Integration", "module", "integration/src", "Integration-related functionality."),
    ],
    "dependencies": [
        ("Admin", "Core", "module dependency", "Administrative functionality likely depends on core commerce services and domain model."),
        ("Broadleaf Admin Module", "Broadleaf Open Admin Platform", "module dependency", "Admin module likely builds on the open admin platform."),
        ("Broadleaf Content Management Module", "Broadleaf Open Admin Platform", "module dependency", "Content management likely uses shared admin platform functionality."),
        ("Broadleaf Framework Web", "Broadleaf Framework", "module dependency", "Web framework functionality depends on the core framework."),
        ("Broadleaf Profile Web", "Broadleaf Profile", "module dependency", "Profile web functionality depends on profile domain functionality."),
        ("Core", "Common", "shared dependency", "Core modules likely depend on shared common utilities."),
        ("Admin", "Common", "shared dependency", "Admin modules likely depend on shared common utilities."),
        ("Integration", "Core", "integration dependency", "Integration code likely interacts with core commerce functionality."),
    ],
    "modernization_findings": [
        (
            "Framework modernization",
            "Application appears to rely on older Java/Spring-era patterns common in legacy enterprise applications.",
            "Major framework upgrades may require broad configuration, dependency, and API changes.",
            "Start with dependency inventory, build baseline, and isolate one low-risk module before attempting major framework upgrades.",
        ),
        (
            "Module coupling",
            "Commerce applications often contain tight coupling between web, service, persistence, and configuration layers.",
            "AI-generated changes may miss downstream impact across modules.",
            "Use dependency context before refactoring shared services or domain objects.",
        ),
        (
            "Build complexity",
            "Large multi-module Java projects often have fragile build and dependency graphs.",
            "A library upgrade can introduce transitive dependency conflicts or test failures.",
            "Modernize incrementally and validate with build/test feedback after each change.",
        ),
        (
            "AI-assisted development",
            "Generic AI recommendations may be plausible but lack application-specific risk awareness.",
            "Without grounded context, AI may recommend unsafe broad changes.",
            "Use MCP tools to retrieve architecture, dependency, and modernization risk context before modifying code.",
        ),
    ],
    "code_artifacts": [
        ("module", "broadleaf-admin-functional-tests", "admin/broadleaf-admin-functional-tests", None, "Functional tests for admin behavior."),
        ("module", "broadleaf-admin-module", "admin/broadleaf-admin-module", None, "Core admin module."),
        ("module", "broadleaf-contentmanagement-module", "admin/broadleaf-contentmanagement-module", None, "Content management module."),
        ("module", "broadleaf-open-admin-platform", "admin/broadleaf-open-admin-platform", None, "Open admin platform module."),
        ("module", "common", "common/src", None, "Shared source code."),
        ("module", "broadleaf-framework", "core/broadleaf-framework", None, "Core framework module."),
        ("module", "broadleaf-framework-web", "core/broadleaf-framework-web", None, "Web framework module."),
        ("module", "broadleaf-profile", "core/broadleaf-profile", None, "Profile domain module."),
        ("module", "broadleaf-profile-web", "core/broadleaf-profile-web", None, "Profile web module."),
        ("module", "integration", "integration/src", None, "Integration source code."),
    ],
}

with sqlite3.connect(DB_PATH) as conn:
    conn.executescript(schema)

    conn.executemany(
        "INSERT INTO components (name, type, path, description) VALUES (?, ?, ?, ?)",
        seed_data["components"],
    )

    conn.executemany(
        "INSERT INTO dependencies (source, target, dependency_type, notes) VALUES (?, ?, ?, ?)",
        seed_data["dependencies"],
    )

    conn.executemany(
        "INSERT INTO modernization_findings (area, finding, risk, recommendation) VALUES (?, ?, ?, ?)",
        seed_data["modernization_findings"],
    )

    conn.executemany(
        "INSERT INTO code_artifacts (artifact_type, name, path, package, notes) VALUES (?, ?, ?, ?, ?)",
        seed_data["code_artifacts"],
    )

print(f"Created context database at: {DB_PATH}")