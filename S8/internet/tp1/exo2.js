const onValidateText = () => {
  const text = document.querySelector("#enterText").value;
  const formattedText = formatText(text);
  const list = document.querySelector("#list_text");
  const listBase = document.querySelector("#list_base");
  list.replaceChildren();
  listBase.replaceChildren();
  formattedText.forEach((word, index) => {
    list.appendChild(createWord(word, formattedText.length, index));
    listBase.appendChild(createWord(word, formattedText.length, index, true));
  });
};

const formatText = (text) => {
  return text
    .split(" ")
    .map((word) =>
      word.replace(
        /(~|`|!|@|#|$|%|^|&|\*|\(|\)|{|}|\[|\]|;|:|\"|'|<|,|\.|>|\?|\/|\\|\||-|_|\+|=)/g,
        "",
      ),
    );
};

const createWord = (word, nb_words, index, withoutColor = false) => {
  const span = document.createElement("span");
  span.textContent = word;
  if (!withoutColor) {
    span.style.color = `rgb(${nb_words ** 2 % 256}, ${word.length ** 3 % 256}, ${(10 * index) % 256})`;
  }
  return span;
};
