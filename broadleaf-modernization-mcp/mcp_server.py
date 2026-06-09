from pathlib import Path
import sqlite3
from mcp.server.fastmcp import FastMCP

mcp = FastMCP("broadleaf-modernization-context")

BASE_DIR = Path(__file__).resolve().parent
DB_PATH = BASE_DIR / "data" / "app_context.db"
CONTEXT_DIR = BASE_DIR / "context"


def query_db(sql: str, params: tuple = ()) -> list[dict]:
    with sqlite3.connect(DB_PATH) as conn:
        conn.row_factory = sqlite3.Row
        return [dict(row) for row in conn.execute(sql, params).fetchall()]


@mcp.tool()
def get_application_overview() -> str:
    """Return the Broadleaf architecture summary."""
    path = CONTEXT_DIR / "architecture.md"
    if not path.exists():
        return "No architecture summary found."
    return path.read_text()


@mcp.tool()
def get_components() -> list[dict]:
    """Return known Broadleaf modules and components."""
    return query_db(
        """
        SELECT name, type, path, description
        FROM components
        ORDER BY type, name
        """
    )


@mcp.tool()
def get_component_dependencies(component_name: str) -> list[dict]:
    """Return dependency context for a Broadleaf component or module."""
    pattern = f"%{component_name}%"
    return query_db(
        """
        SELECT source, target, dependency_type, notes
        FROM dependencies
        WHERE source LIKE ? OR target LIKE ?
        ORDER BY source, target
        """,
        (pattern, pattern),
    )


@mcp.tool()
def get_modernization_risks() -> list[dict]:
    """Return known modernization risks and recommendations."""
    return query_db(
        """
        SELECT area, finding, risk, recommendation
        FROM modernization_findings
        ORDER BY area
        """
    )


@mcp.tool()
def search_modernization_context(keyword: str) -> list[dict]:
    """Search modernization findings by keyword."""
    pattern = f"%{keyword}%"
    return query_db(
        """
        SELECT area, finding, risk, recommendation
        FROM modernization_findings
        WHERE area LIKE ?
           OR finding LIKE ?
           OR risk LIKE ?
           OR recommendation LIKE ?
        ORDER BY area
        """,
        (pattern, pattern, pattern, pattern),
    )

@mcp.tool()
def get_modernization_plan() -> str:
    """
    Return a recommended modernization approach.
    """

    return """
Recommended modernization approach:

1. Inventory dependencies and framework versions.
2. Identify highly coupled modules.
3. Prioritize low-risk modernization targets.
4. Validate changes through build and test automation.
5. Perform incremental upgrades rather than large-scale rewrites.
6. Use architecture and dependency context before AI-assisted refactoring.
"""

@mcp.tool()
def analyze_module(module_name: str) -> dict:
    """
    Return contextual information about a Broadleaf module.
    """

    components = query_db(
        """
        SELECT name, type, path, description
        FROM components
        WHERE name LIKE ?
        """,
        (f"%{module_name}%",),
    )

    deps = query_db(
        """
        SELECT source, target, dependency_type
        FROM dependencies
        WHERE source LIKE ?
           OR target LIKE ?
        """,
        (f"%{module_name}%", f"%{module_name}%"),
    )

    return {
        "components": components,
        "dependencies": deps,
        "recommendation":
            "Review dependency relationships before performing modernization activities."
    }

if __name__ == "__main__":
    mcp.run()