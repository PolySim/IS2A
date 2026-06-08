const get_random_number = ({ min, max }) => {
  return Math.floor(Math.random() * (max - min + 1)) + min;
};

const calculate_input = () => {
  const input = document.querySelector("#input_text");
  if (!input) return;
  const elt = document.querySelector("#histo");
  if (!elt) return;

  const value = input.value;
  const values = value.split("");

  Plotly.newPlot(elt,  [{
     x: values,
     type: 'histogram',
   }]);
  apply_mean(values);
};

const apply_mean = (values) => {
  const meanContainer = document.querySelector("#mean");
  if (!meanContainer) return;
  meanContainer.textContent = values.length / new Set(values).size;
};
