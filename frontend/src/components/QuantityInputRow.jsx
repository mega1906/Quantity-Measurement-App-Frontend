export function QuantityInputRow({ label, quantity, units, onChange }) {
  return (
    <div className="quantity-row">
      <label className="field">
        <span>{label}</span>
        <input
          type="number"
          step="any"
          value={quantity.value}
          onChange={(event) => onChange('value', event.target.value)}
          placeholder="Enter value"
          required
        />
      </label>
      <label className="field">
        <span>Unit</span>
        <select value={quantity.unit} onChange={(event) => onChange('unit', event.target.value)}>
          {units.map((unit) => (
            <option key={unit} value={unit}>
              {unit}
            </option>
          ))}
        </select>
      </label>
    </div>
  );
}
