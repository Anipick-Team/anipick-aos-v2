#!/usr/bin/env python3
"""docs/qa/testcases/**/*.md (docs/conventions/qa-testcase-format.md 형식) 을 읽어서
화면 하나 = 시트 하나로 엑셀 파일을 만든다.

사용법:
    python3 docs/qa/generate_xlsx.py                       # testcases/ 전체를 한 워크북으로
    python3 docs/qa/generate_xlsx.py testcases/mypage       # 특정 폴더만
    python3 docs/qa/generate_xlsx.py testcases/mypage/setting-main.md testcases/mypage/setting-detail.md
    python3 docs/qa/generate_xlsx.py -o out.xlsx ...        # 출력 파일 지정

기본 출력 경로는 레포 밖 `/Users/jparkbro/Documents/WorkSpace/QA/AniPick_QA_TestCases.xlsx` — xlsx는
그때그때 다시 뽑는 산출물이라 레포에 커밋하지 않는다.

의존성: openpyxl (pip install openpyxl)
"""
from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

from openpyxl import Workbook
from openpyxl.styles import Alignment, Border, Font, PatternFill, Side
from openpyxl.utils import get_column_letter
from openpyxl.worksheet.worksheet import Worksheet

HEADER_FILL = PatternFill("solid", fgColor="1F2937")
HEADER_FONT = Font(bold=True, color="FFFFFF")
SECTION_FILL = PatternFill("solid", fgColor="E5E7EB")
SECTION_FONT = Font(bold=True, size=12)
TITLE_FONT = Font(bold=True, size=14)
THIN = Side(style="thin", color="D1D5DB")
CELL_BORDER = Border(left=THIN, right=THIN, top=THIN, bottom=THIN)
WRAP = Alignment(wrap_text=True, vertical="top", horizontal="left")

TABLE_ROW_RE = re.compile(r"^\s*\|(.+)\|\s*$")
SEPARATOR_CELL_RE = re.compile(r"^\s*:?-{2,}:?\s*$")


def parse_markdown(path: Path) -> list[tuple[str, list[str], list[list[str]]]]:
    """마크다운 파일 하나를 (섹션 제목, 헤더 컬럼들, 데이터 행들) 리스트로 파싱한다.
    `##`/`###` 헤딩을 섹션 구분자로 쓰고, 그 아래 첫 번째 표만 그 섹션 표로 인식한다.
    섹션 헤딩 없이 파일 최상단(H1) 바로 아래 표만 있으면 섹션 제목은 빈 문자열."""
    lines = path.read_text(encoding="utf-8").splitlines()
    sections: list[tuple[str, list[str], list[list[str]]]] = []
    current_section = ""
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.startswith("## "):
            current_section = re.sub(r"`([^`]*)`", r"\1", line[3:].strip())
            i += 1
            continue
        if line.startswith("### "):
            current_section = re.sub(r"`([^`]*)`", r"\1", line[4:].strip())
            i += 1
            continue

        m = TABLE_ROW_RE.match(line)
        if m and i + 1 < len(lines):
            sep_m = TABLE_ROW_RE.match(lines[i + 1])
            sep_cells = [c.strip() for c in sep_m.group(1).split("|")] if sep_m else []
            if sep_m and all(SEPARATOR_CELL_RE.match(c) for c in sep_cells if c != ""):
                header = [c.strip() for c in m.group(1).split("|")]
                rows: list[list[str]] = []
                j = i + 2
                while j < len(lines):
                    row_m = TABLE_ROW_RE.match(lines[j])
                    if not row_m:
                        break
                    cells = [c.strip().replace("\\|", "|") for c in row_m.group(1).split("|")]
                    # 셀 안 인라인 코드(`...`) 백틱만 제거 - 엑셀에선 굳이 안 살려도 됨
                    cells = [re.sub(r"`([^`]*)`", r"\1", c) for c in cells]
                    rows.append(cells)
                    j += 1
                sections.append((current_section, header, rows))
                i = j
                continue
        i += 1
    return sections


def first_h1(path: Path) -> str:
    for line in path.read_text(encoding="utf-8").splitlines():
        if line.startswith("# "):
            return line[2:].strip()
    return path.stem


def sheet_name_for(path: Path) -> str:
    name = path.stem
    return name[:31]


def write_sheet(ws: Worksheet, title: str, sections: list[tuple[str, list[str], list[list[str]]]]) -> None:
    row = 1
    ws.cell(row=row, column=1, value=title).font = TITLE_FONT
    row += 2

    max_cols = max((len(h) for _, h, _ in sections), default=1)

    for section_title, header, data_rows in sections:
        if section_title:
            ws.cell(row=row, column=1, value=section_title).font = SECTION_FONT
            for c in range(1, max_cols + 1):
                ws.cell(row=row, column=c).fill = SECTION_FILL
            row += 1

        for c, name in enumerate(header, start=1):
            cell = ws.cell(row=row, column=c, value=name)
            cell.font = HEADER_FONT
            cell.fill = HEADER_FILL
            cell.alignment = WRAP
            cell.border = CELL_BORDER
        header_row = row
        row += 1

        for data_row in data_rows:
            for c, value in enumerate(data_row, start=1):
                cell = ws.cell(row=row, column=c, value=value)
                cell.alignment = WRAP
                cell.border = CELL_BORDER
            row += 1

        ws.freeze_panes = ws.cell(row=header_row + 1, column=1).coordinate
        row += 1  # 섹션 사이 빈 줄

    widths = [14, 26, 20, 34, 26, 34, 26]
    for c in range(1, max_cols + 1):
        ws.column_dimensions[get_column_letter(c)].width = widths[c - 1] if c - 1 < len(widths) else 22


def collect_md_files(args: list[str]) -> list[Path]:
    if not args:
        base = Path(__file__).parent / "testcases"
        return sorted(base.rglob("*.md"))
    paths: list[Path] = []
    for a in args:
        p = Path(a)
        if not p.is_absolute():
            p = Path(__file__).parent / a if not p.exists() else p
        if p.is_dir():
            paths.extend(sorted(p.rglob("*.md")))
        elif p.suffix == ".md":
            paths.append(p)
        else:
            print(f"skip (not a .md file): {p}", file=sys.stderr)
    return paths


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("paths", nargs="*", help="변환할 .md 파일 또는 폴더 (기본: docs/qa/testcases/ 전체)")
    parser.add_argument(
        "-o", "--output",
        default="/Users/jparkbro/Documents/WorkSpace/QA/AniPick_QA_TestCases.xlsx",
        help="출력 xlsx 경로 (기본: 레포 밖 QA 폴더 — 결과물을 레포에 커밋하지 않기 위함)",
    )
    args = parser.parse_args()

    md_files = collect_md_files(args.paths)
    if not md_files:
        print("변환할 .md 파일을 찾지 못했습니다.", file=sys.stderr)
        sys.exit(1)

    wb = Workbook()
    wb.remove(wb.active)

    used_names: set[str] = set()
    for path in md_files:
        sections = parse_markdown(path)
        if not sections:
            print(f"skip (표 없음): {path}", file=sys.stderr)
            continue
        name = sheet_name_for(path)
        base_name = name
        n = 2
        while name in used_names:
            suffix = f"_{n}"
            name = base_name[: 31 - len(suffix)] + suffix
            n += 1
        used_names.add(name)

        ws = wb.create_sheet(title=name)
        write_sheet(ws, first_h1(path), sections)
        print(f"{path} -> sheet '{name}' ({sum(len(r) for _, _, r in sections)}행)")

    out_path = Path(args.output)
    wb.save(out_path)
    print(f"\n생성됨: {out_path.resolve()}")


if __name__ == "__main__":
    main()
