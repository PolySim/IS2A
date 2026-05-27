const gridMatrix = new Array(25).fill(new Array(25).fill(0));

const generateRandomColor = () => {
  return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
};

const generateButtonColors = (size = 10) => {
  const gray = document.getElementById("gray").checked;

  if (gray) {
    return [
      ...Array.from({ length: size - 1 }, (_, index) => {
        const grayValue = Math.floor((index / size) * 255);
        return `rgb(${grayValue}, ${grayValue}, ${grayValue})`;
      }),
      "white",
    ];
  }

  return [
    ...Array.from({ length: size - 1 }, () => generateRandomColor()),
    "white",
  ];
};

const onReset = () => {
  gridMatrix.forEach((row, rowIndex) =>
    row.forEach((_, colIndex) => {
      const cell = document.querySelector(
        `[row="${rowIndex}"][coll="${colIndex}"]`,
      );
      cell.style.backgroundColor = "white";
    }),
  );
};

const onSelectColor = (color) => {
  const currentColor = document.getElementById("current-color");
  currentColor.style.backgroundColor = color;
};

const onApplyColor = (rowIndex, colIndex) => {
  const color = document.getElementById("current-color").style.backgroundColor;
  const cell = document.querySelector(
    `[row="${rowIndex}"][coll="${colIndex}"]`,
  );
  cell.style.backgroundColor = color;
};

const generateGrid = () => {
  const grid = document.getElementById("grid");

  gridMatrix.forEach((row, rowIndex) =>
    row.forEach((cell, cellIndex) => {
      const cellElement = document.createElement("div");
      cellElement.textContent = "";
      cellElement.setAttribute("row", rowIndex);
      cellElement.setAttribute("coll", cellIndex);
      cellElement.addEventListener("click", () =>
        onApplyColor(rowIndex, cellIndex),
      );
      grid.appendChild(cellElement);
    }),
  );
};

const generateButtons = (regenerate = false) => {
  const isGray = document.getElementById("gray").checked;

  if (regenerate && isGray) {
    alert("Vous ne pouvez pas faire ça quand vous êtes en nuance de gris");
    return;
  }

  const selectBtn = document.getElementById("select-btn");

  selectBtn.replaceChildren();
  const nbColors = document.getElementById("nb_colors");
  const buttons = generateButtonColors(parseInt(nbColors.value));

  buttons.forEach((button, index) => {
    const buttonElement = document.createElement("button");
    buttonElement.textContent = button;
    buttonElement.style.backgroundColor = button;
    buttonElement.style.color =
      index === buttons.length - 1 ? "black" : "white";
    buttonElement.setAttribute("index", index);
    buttonElement.setAttribute("color", button);
    buttonElement.addEventListener("click", () => onSelectColor(button));
    selectBtn.appendChild(buttonElement);
    buttons[index] = buttonElement;
  });
};

window.addEventListener("load", () => {
  generateGrid();
  generateButtons();
});
