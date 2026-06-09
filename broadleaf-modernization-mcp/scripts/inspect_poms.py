from pathlib import Path
import sqlite3
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
DB_PATH = ROOT / "broadleaf-modernization-mcp" / "data" / "app_context.db"

NS = {"m": "http://maven.apache.org/POM/4.0.0"}

def text(node, path):
    found = node.find(path, NS)
    return found.text.strip() if found is not None and found.text else None

def parse_pom(pom_path):
    tree = ET.parse(pom_path)
    root = tree.getroot()

    artifact_id = text(root, "m:artifactId")
    group_id = text(root, "m:groupId") or text(root, "m:parent/m:groupId")
    version = text(root, "m:version") or text(root, "m:parent/m:version")
    packaging = text(root, "m:packaging") or "jar"

    deps = []
    for dep in root.findall("m:dependencies/m:dependency", NS):
        deps.append({
            "pom_path": str(pom_path.relative_to(ROOT)),
            "module": artifact_id,
            "group_id": text(dep, "m:groupId"),
            "artifact_id": text(dep, "m:artifactId"),
            "version": text(dep, "m:version"),
            "scope": text(dep, "m:scope"),
        })

    return {
        "pom_path": str(pom_path.relative_to(ROOT)),
        "group_id": group_id,
        "artifact_id": artifact_id,
        "version": version,
        "packaging": packaging,
        "dependencies": deps,
    }

with sqlite3.connect(DB_PATH) as conn:
    conn.execute("""
        CREATE TABLE IF NOT EXISTS maven_modules (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            pom_path TEXT,
            group_id TEXT,
            artifact_id TEXT,
            version TEXT,
            packaging TEXT
        )
    """)

    conn.execute("""
        CREATE TABLE IF NOT EXISTS maven_dependencies (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            pom_path TEXT,
            module TEXT,
            group_id TEXT,
            artifact_id TEXT,
            version TEXT,
            scope TEXT
        )
    """)

    conn.execute("DELETE FROM maven_modules")
    conn.execute("DELETE FROM maven_dependencies")

    for pom in ROOT.glob("**/pom.xml"):
        if "broadleaf-modernization-mcp" in str(pom):
            continue

        parsed = parse_pom(pom)

        conn.execute(
            """
            INSERT INTO maven_modules
            (pom_path, group_id, artifact_id, version, packaging)
            VALUES (?, ?, ?, ?, ?)
            """,
            (
                parsed["pom_path"],
                parsed["group_id"],
                parsed["artifact_id"],
                parsed["version"],
                parsed["packaging"],
            ),
        )

        for dep in parsed["dependencies"]:
            conn.execute(
                """
                INSERT INTO maven_dependencies
                (pom_path, module, group_id, artifact_id, version, scope)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                (
                    dep["pom_path"],
                    dep["module"],
                    dep["group_id"],
                    dep["artifact_id"],
                    dep["version"],
                    dep["scope"],
                ),
            )

print(f"Updated dependency inventory in {DB_PATH}")