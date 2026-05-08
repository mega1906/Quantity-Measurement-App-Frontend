# Quantity Measurement App - ReactJS

## Unit Conversion & Comparison Frontend

### Features

- **Unit Conversion**: Convert between different units of measurement
  - Length, Weight, Temperature, and Volume
  - Real-time conversion calculations
  
- **Quantity Comparison**: Compare values across different units
  
- **Arithmetic Operations**: Perform calculations (Add, Subtract, Multiply, Divide)
  
- **History Tracking**: 
  - View complete operation history
  - Filter by operation type (Convert, Compare, Arithmetic)
  - Filter by unit type
  - Delete individual history items
  - Clear all history

- **Interactive UI**: 
  - User-friendly form panels
  - Real-time result display with highlighting
  - Conversion reference guide

## Project Structure

```
src/
├── components/       
│   ├── AppHeader.jsx   
│   ├── CompareForm.jsx 
│   ├── ConversionForm.jsx 
│   ├── ConversionReference.jsx 
│   ├── FormPanel.jsx   
│   ├── HistoryPanel.jsx
│   ├── QuantityInputRow.jsx
│   ├── ResultBanner.jsx 
│   ├── TemperatureForm.jsx 
│   └── WeightedArithmeticForm.jsx 
├── api/
│   └── quantityApi.js  
├── App.js         
├── App.css  
├── index.js    
└── index.css       
```

## Installation & Setup

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:
   ```bash
   npm start
   ```
   The app will open at [http://localhost:3000](http://localhost:3000)

3. Build for production:
   ```bash
   npm run build
   ```