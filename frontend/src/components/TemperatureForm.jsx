import { useEffect, useState } from 'react';
import { FormPanel } from './FormPanel';
import { QuantityInputRow } from './QuantityInputRow';

export function TemperatureForm({ units, loading, onSubmit }) {
  const [formState, setFormState] = useState(createInitialState(units));

  useEffect(() => {
    setFormState(createInitialState(units));
  }, [units]);

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit(formState);
  }

  return (
    <FormPanel
      title="Convert temperature"
      subtitle="Use the dedicated temperature endpoint for Celsius and Fahrenheit."
    >
      <form className="form-grid" onSubmit={handleSubmit}>
        <QuantityInputRow
          label="Temperature"
          quantity={formState}
          units={units}
          onChange={(field, value) =>
            setFormState((current) => ({
              ...current,
              [field]: value,
            }))
          }
        />
        <label className="field">
          <span>Target scale</span>
          <select
            value={formState.targetUnit}
            onChange={(event) =>
              setFormState((current) => ({
                ...current,
                targetUnit: event.target.value,
              }))
            }
          >
            {units.map((unit) => (
              <option key={unit} value={unit}>
                {unit}
              </option>
            ))}
          </select>
        </label>
        <button className="primary-button" type="submit" disabled={loading}>
          {loading ? 'Converting...' : 'Convert temperature'}
        </button>
      </form>
    </FormPanel>
  );
}

function createInitialState(units) {
  const defaultUnit = units[0] || '';
  const fallbackTarget = units[1] || defaultUnit;

  return {
    value: '',
    unit: defaultUnit,
    targetUnit: fallbackTarget,
  };
}
