const get_random_number = ({ min, max }) => {
  return Math.floor(Math.random() * (max - min + 1)) + min;
};

const apply_random_number = () => {
  const range = document.querySelector("#range");
  if (!range) return;
  const min = parseInt(range.value.split("_")[0]);
  const max = parseInt(range.value.split("_")[1]);

  const elt = document.querySelector("#histo");
  if (!elt) return;
  const data = []
  for (let i = 0; i < 501; i++)
    data.push( get_random_number({ min, max }))

  Plotly.newPlot(elt,  [{
     x: data,
     type: 'histogram',
   }]);
};

window.addEventListener("load", () => {
  apply_random_number();
});
