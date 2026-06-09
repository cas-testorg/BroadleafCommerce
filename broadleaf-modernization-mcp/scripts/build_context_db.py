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
        ("Broadleaf Commerce", "application", ".", "Legacy Java commerce application with modular ecommerce capabilities."),
        ("Admin", "module", "admin", "Administrative UI and management functionality."),
        ("Core", "module", "core", "Core commerce domain logic and shared services."),
        ("API", "module", "api", "Public API and integration-facing code."),
        ("Web", "module", "site/web", "Customer-facing storefront web layer."),
    ],
    "dependencies": [
        ("Web", "Core", "module dependency", "Storefront depends on core commerce services and domain logic."),
        ("Admin", "Core", "module dependency", "Admin functionality depends on core domain and service layers."),
        ("API", "Core", "module dependency", "API layer exposes core commerce capabilities."),
        ("Core", "Database", "persistence dependency", "Core services interact with relational persistence layer."),
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
        ("module", "Admin", "admin", None, "Likely admin-facing functionality."),
        ("module", "Core", "core", None, "Likely core domain and service functionality."),
        ("module", "API", "api", None, "Likely external-facing API functionality."),
        ("module", "Web", "site/web", None, "Likely storefront web functionality."),
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