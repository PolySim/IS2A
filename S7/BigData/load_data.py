def add_or_replace(lst: list, index: int, value) -> list:
    if index >= len(lst):
        lst.append(value)
    else:
        lst[index] = value
    return lst


def format_pages(pages: list[str]) -> list[str]:
    override_pages = []
    result_pages = []
    override_index = 0
    current_index = 0
    for page in pages:
        if page == "<":
            add_or_replace(
                result_pages, current_index, override_pages[override_index - 2]
            )
            override_index -= 1
        else:
            add_or_replace(override_pages, override_index, page)
            add_or_replace(result_pages, current_index, page)
            override_index += 1

        current_index += 1

    return result_pages


def load_data(filePath: str = "paths_finished.tsv"):
    data = []
    with open(filePath, "r") as file:
        for line in file:
            if line.startswith("#"):
                continue
            parts = line.strip().split("\t")
            if len(parts) != 5:
                continue
            pages = parts[3].split(";")
            data.append(format_pages(pages))

    return data


if __name__ == "__main__":
    data = load_data("filtered.tsv")
    for e in data[-1]:
        print(e)
