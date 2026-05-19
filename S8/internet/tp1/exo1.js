let global_mise = 0;

const generateRandomNumber = () => {
  return Math.floor(Math.random() * 21);
};

const onValidGlobalMise = () => {
  global_mise = parseInt(document.querySelector("#global_mise").value);
  if (Number.isNaN(global_mise) || global_mise < 0) {
    alert("Il faut mettre un nombre positif");
    return;
  }
  document.querySelector(".start_mise").style.display = "none";
  document.querySelector(".game").style.display = "flex";

  updateAmount(global_mise);
};

const updateAmount = (amount) => {
  document.querySelector("#amount").textContent = amount;
  document.querySelector("#mise").max = amount;
};

const onValidate = () => {
  const mise = parseInt(document.querySelector("#mise").value);
  const number = parseInt(document.querySelector("#nombre").value);
  if (mise > global_mise) {
    alert(`La mise doit être inférieure à ${global_mise}`);
    return;
  }

  const randomNumber = generateRandomNumber();

  if (randomNumber > number || number - randomNumber > 5) {
    onModifyResult(
      `Le nombre à deviner était ${randomNumber}, tu as perdu ${mise}€`,
    );
    global_mise -= mise;
    updateAmount(global_mise);
  } else {
    onModifyResult(
      `Le nombre à deviner était ${randomNumber}, tu as gagné ${2 * mise}€`,
    );
    global_mise += 2 * mise;
    updateAmount(global_mise);
  }
  onResetInput();
  if (global_mise <= 0) onResetGame();
};

const onModifyResult = (message) =>
  (document.querySelector("#result").textContent = message);

const onResetInput = () => {
  document.querySelector("#mise").value = "1";
  document.querySelector("#nombre").value = "0";
};

const onResetGame = () => {
  document.querySelector(".start_mise").style.display = "flex";
  document.querySelector(".game").style.display = "none";
  document.querySelector("#global_mise").value = "10";
  onModifyResult("");
};
