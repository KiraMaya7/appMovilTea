# AppTea

App para comunicación con imágenes para personas con TEA.

## Pantallas

- **Welcome** - Pantalla de inicio (elige Tutor o Niño)
- **Pin** - Ingresar PIN 1234 para entrar como Tutor
- **TutorPanel** - Muestra 8 categorías (Alimentos, Bebidas, etc.)
- **CategoryDetail** - Lista de imágenes de cada categoría

## Lo que funciona

- Navegación entre pantallas
- Ver categorías y sus imágenes
- Agregar, editar y eliminar imágenes (solo en memoria, falta testing)

## Lo que falta

- Guardar imágenes permanentemente de local o nube 
- Grabar y reproducir audio lical o nube
- Panel del Niño integracion en despues

## Cómo probar

1. Abre la app
2. Selecciona "Tutor"
3. PIN: 1234
4. Entra a Alimentos o Bebidas
5. Prueba agregar/editar/eliminar imágenes forma local

## Estructura
data/
├── models/ (Category, ImageItem)
├── mock/ (datos de ejemplo)
ui/
├── components/ (CategoryCard, ImageItemCard)
├── screens/ (Welcome, Pin, TutorPanel, CategoryDetail)
└── navigation/ (AppNavigation)
