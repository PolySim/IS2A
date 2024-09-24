# This is the IS2A repository for the three years in Polytech Lille

## Installation

```bash
git clone https://github.com/simon987/IS2A.git
cd IS2A
pnpm install
```

## Run

### Python

```bash
pnpm run dev:python S5/algo/TP1
# sh script/start.python.sh {{path/to/file/or/folder}}
```

### C

```bash
pnpm run dev:c S5/algo/TP1/test.c
# sh script/start.c.sh {{path/to/file}}
```

This will compile the file and run it.
The file will be compiled in the `bin` folder.