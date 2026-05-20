const SKILLS = {
  frontend: [
    {
      title: "NextJS",
      image_src: "skill/next.png",
      background_linear: "linear-gradient(45deg,#ffcca8 .21%,#fffa87 99.93%)",
      doc_url: "https://nextjs.org/",
    },
    {
      title: "Angular",
      image_src: "skill/angular.svg",
      background_linear:
        "linear-gradient(225.22deg,#93ffff -.09%,#59a5ff 100.01%)",
      doc_url: "https://angular.dev/",
    },
    {
      title: "Tanstack",
      image_src: "skill/tanstack.png",
      background_linear:
        "linear-gradient(225.22deg,#aae0ff -.01%,#5affce 100.08%)",
      doc_url: "https://tanstack.com/",
    },
    {
      title: "TailwindCSS",
      image_src: "skill/tailwind.png",
      background_linear: "linear-gradient(225deg,#b1b5ff .07%,#4260ff 99.79%)",
      doc_url: "https://tailwindcss.com/",
    },
    {
      title: "Zod",
      image_src: "skill/zod.png",
      background_linear:
        "linear-gradient(225.22deg,#ffb5b5 .06%,#ff4fce 50.11%,#d256ff 100.15%)",
      doc_url: "https://zod.dev/",
    },
  ],
  backend: [
    {
      title: ".NET",
      image_src: "skill/dotnet.svg",
      background_linear:
        "linear-gradient(45deg,#89e3ff .29%,#cfffb8 50.14%,#ffe177)",
      doc_url: "https://dotnet.microsoft.com",
    },
    {
      title: "NestJS",
      image_src: "skill/nestjs.svg",
      background_linear:
        "linear-gradient(90deg, hsla(31, 90%, 76%, 1) 0%, hsla(302, 82%, 76%, 1) 100%)",
      doc_url: "https://nestjs.com/",
    },
    {
      title: "Docker",
      image_src: "skill/docker.png",
      background_linear:
        "linear-gradient(90deg, hsla(17, 82%, 53%, 1) 0%, hsla(319, 74%, 64%, 1) 100%)",
      doc_url: "https://www.docker.com/",
    },
    {
      title: "Rust",
      image_src: "skill/rust.svg",
      background_linear: "linear-gradient(225deg,#ffc2f9,#c03cff 99.71%)",
      doc_url: "https://www.rust-lang.org/",
    },
    {
      title: "SQL",
      image_src: "skill/sql.svg",
      background_linear: "linear-gradient(225deg,#b1b5ff .07%,#4260ff 99.79%)",
      doc_url: "https://sql.sh/",
    },
    {
      title: "MongoDB",
      image_src: "skill/mongo.png",
      background_linear: "linear-gradient(45deg,#ffcca8 .21%,#fffa87 99.93%)",
      doc_url: "https://www.mongodb.com/fr-fr",
    },
  ],
};

const PROJECTS = [
  {
    name: "Professional photographer",
    demoPath: "https://angelinedesdevises.fr/",
    sourceCodePath: "https://github.com/PolySim/Angeline.Monorepo",
    imgPath: "project/angeline.png",
  },
  {
    name: "Clone Apple MacBook",
    demoPath: "https://polysim.github.io/Apple_MacbookAir/",
    sourceCodePath: "https://github.com/PolySim/Apple_MacbookAir",
    imgPath: "project/macBookAir.png",
  },
  {
    name: "Create your documentation",
    demoPath: "https://docs.simondesdevises.com/",
    sourceCodePath: "https://github.com/PolySim/Create-Documentation",
    imgPath: "project/documentation.png",
  },
  {
    name: "65 Pasion Montagne",
    demoPath: "https://65passionmontagne.simondesdevises.com/",
    sourceCodePath: "https://github.com/PolySim/65.Monorepo",
    imgPath: "project/65PassionMontagne.png",
  },
];

const toggleFocus = (id) => {
  const currentFocus = document.querySelector(".current");
  currentFocus.className = currentFocus.className.replace("current", "");
  const newFocus = document.getElementById(id);
  newFocus.className = "current";

  const part = id.replace("btn_", "");
  const section = document.getElementById(part);
  section.scrollIntoView({ behavior: "smooth" });
};

const hydradeSkills = () => {
  const frontend = document.querySelector("#frontend");
  const backend = document.querySelector("#backend");

  hydradeSkill(frontend, SKILLS.frontend);
  hydradeSkill(backend, SKILLS.backend);
};

const hydradeSkill = (container, skills) => {
  skills.forEach((skill) => {
    const containerSkill = document.createElement("div");
    const skillBox = document.createElement("div");
    const imageContainer = document.createElement("div");
    const image = document.createElement("img");
    const link = document.createElement("a");
    containerSkill.className = "group";

    skillBox.style.background = skill.background_linear;
    skillBox.className = "skill-box-after";
    skillBox.setAttribute("data-content", skill.title);

    image.src = skill.image_src;
    image.alt = skill.title;

    link.href = skill.doc_url;
    link.style.background = skill.background_linear;

    imageContainer.appendChild(image);
    skillBox.appendChild(imageContainer);
    skillBox.appendChild(link);
    containerSkill.appendChild(skillBox);
    container.appendChild(containerSkill);
  });
};

const hydradeProjects = () => {
  const projectBlock = document.querySelector("#project_block");

  PROJECTS.forEach((project) => {
    const projectBox = document.createElement("div");
    const name = document.createElement("h6");
    const image = document.createElement("img");
    const demoLink = document.createElement(project.demoPath ? "a" : "div");
    const sourceCodeLink = document.createElement("a");
    const linkContainer = document.createElement("div");

    name.innerHTML = project.name;
    image.src = project.imgPath;
    image.alt = project.name;
    demoLink.innerHTML = project.demoPath ? "Demo" : "";
    demoLink.className = "demo-link";
    demoLink.href = project.demoPath;
    sourceCodeLink.innerHTML = "Source Code";
    sourceCodeLink.className = "source-code-link";
    sourceCodeLink.href = project.sourceCodePath;

    linkContainer.appendChild(demoLink);
    linkContainer.appendChild(sourceCodeLink);

    projectBox.appendChild(name);
    projectBox.appendChild(image);
    projectBox.appendChild(linkContainer);

    projectBlock.appendChild(projectBox);
  });
};

const onLoad = () => {
  hydradeSkills();
  hydradeProjects();
};

window.addEventListener("load", onLoad);
