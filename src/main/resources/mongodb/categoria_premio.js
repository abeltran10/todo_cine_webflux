use("todo_cine_dev");

db.categoria_premio.drop();

db.categoria_premio.insertMany([
    {
        premioId: 1,
        titulo: "Goya",
        categorias: [
            { id: 1, nombre: "Mejor película" },
            { id: 2, nombre: "Actor revelación" },
            { id: 3, nombre: "Dirección" },
            { id: 4, nombre: "Música original" },
            { id: 5, nombre: "Dirección de producción" },
            { id: 6, nombre: "Efectos especiales" },
            { id: 7, nombre: "Sonido" },
            { id: 8, nombre: "Maquillaje y peluquería" },
            { id: 9, nombre: "Dirección de fotografía" },
            { id: 10, nombre: "Diseño de vestuario" },
            { id: 11, nombre: "Montaje" },
            { id: 12, nombre: "Dirección artística" },
            { id: 13, nombre: "Actriz revelación" },
            { id: 14, nombre: "Actriz reparto" },
            { id: 15, nombre: "Dirección novel" },
            { id: 16, nombre: "Película Iberoamericana" },
            { id: 17, nombre: "Actriz protagonista" },
            { id: 18, nombre: "Actor protagonista" },
            { id: 19, nombre: "Actor reparto" },
            { id: 20, nombre: "Película de animación" },
            { id: 21, nombre: "Cortometraje de ficción" },
            { id: 22, nombre: "Cortometraje documental" },
            { id: 23, nombre: "Cortometraje de animación" },
            { id: 24, nombre: "Guión adaptado" },
            { id: 25, nombre: "Guión original" },
            { id: 26, nombre: "Película europea" },
            { id: 27, nombre: "Película documental" },
            { id: 30, nombre: "Canción original" }
        ]
    },
    {
        premioId: 2,
        titulo: "Globos de oro",
        categorias: [
            { id: 1, nombre: "Mejor película" },
            { id: 3, nombre: "Dirección" },
            { id: 4, nombre: "Música original" },
            { id: 11, nombre: "Montaje" },
            { id: 14, nombre: "Actriz reparto" },
            { id: 17, nombre: "Actriz protagonista" },
            { id: 18, nombre: "Actor protagonista" },
            { id: 19, nombre: "Actor reparto" },
            { id: 20, nombre: "Película de animación" },
            { id: 24, nombre: "Guión adaptado" },
            { id: 25, nombre: "Guión original" },
            { id: 28, nombre: "Película de habla no inglesa" },
            { id: 29, nombre: "Banda sonora" }
        ]
    },
    {
        premioId: 3,
        titulo: "Oscars",
        categorias: [
            { id: 1, nombre: "Mejor película" },
            { id: 3, nombre: "Dirección" },
            { id: 6, nombre: "Efectos especiales" },
            { id: 7, nombre: "Sonido" },
            { id: 11, nombre: "Montaje" },
            { id: 4, nombre: "Actriz reparto" },
            { id: 17, nombre: "Actriz protagonista" },
            { id: 18, nombre: "Actor protagonista" },
            { id: 19, nombre: "Actor reparto" },
            { id: 20, nombre: "Película de animación" },
            { id: 22, nombre: "Cortometraje documental" },
            { id: 23, nombre: "Cortometraje de animación" },
            { id: 24, nombre: "Guión adaptado" },
            { id: 25, nombre: "Guión original" },
            { id: 28, nombre: "Película de habla no inglesa" },
            { id: 29, nombre: "Banda sonora" },
            { id: 30, nombre: "Canción original" },
            { id: 31, nombre: "Mejor documental" },
            { id: 32, nombre: "Mejor cortometraje" },
            { id: 33, nombre: "Mejor fotografía" },
            { id: 35, nombre: "Mejor casting" }
        ]
    }
]);

db.categoria_premio.createIndex({ premioId: 1 }, { unique: true });

print("✅ categoria_premio cargado correctamente");