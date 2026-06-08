const get_random_number = ({ min, max }) => {
  return Math.floor(Math.random() * (max - min + 1)) + min;
};

const apply_random_number = () => {
  const elt = document.querySelector("#random_number");
  if (!elt) return;
  const random_number = get_random_number({ min: 1, max: 10 });
  elt.textContent = random_number;
};

window.addEventListener("load", () => {
  apply_random_number();
});
