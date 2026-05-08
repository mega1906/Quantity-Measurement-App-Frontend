import { useEffect, useState } from 'react';
import { FormPanel } from './FormPanel';
import { QuantityInputRow } from './QuantityInputRow';

const operations = ['ADD', 'SUBTRACT', 'MULTIPLY', 'DIVIDE'];

export function WeightedArithmeticForm({ measurementType, units, loading, onSubmit }) {
  const [formState, setFormState] = useState(createInitialState(units));

  useEffect(() => {
    setFormState(createInitialState(units));
  }, [units]);

  function handleValueChange(key, field, value) {
    setFormState((current) => ({
      ...current,
      [key]: {
        ...current[key],
        [field]: value,
      },
    }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit(formState);
  }

  return (
    <FormPanel
      title="Run arithmetic"
      subtitle={`Apply add, subtract, multiply, or divide on ${measurementType.toLowerCase()} values.`}
    >
      <form className="form-grid" onSubmit={handleSubmit}>
        <QuantityInputRow
          label="First quantity"
          quantity={formState.thisQuantity}
          units={units}
          onChange={(field, value) => handleValueChange('thisQuantity', field, value)}
        />
        <QuantityInputRow
          label="Second quantity"
          quantity={formState.thatQuantity}
          units={units}
          onChange={(field, value) => handleValueChange('thatQuantity', field, value)}
        />
        <div className="triple-grid">
          <label className="field">
            <span>Operation</span>
            <select
              value={formState.operation}
              onChange={(event) =>
                setFormState((current) => ({
                  ...current,
                  operation: event.target.value,
                }))
              }
            >
              {operations.map((operation) => (
                <option key={operation} value={operation}>
                  {operation}
                </option>
              ))}
            </select>
          </label>
          <label className="field">
            <span>Result unit</span>
            <select
              value={formState.resultUnit}
              onChange={(event) =>
                setFormState((current) => ({
                  ...current,
                  resultUnit: event.target.value,
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
        </div>
        <button className="primary-button" type="submit" disabled={loading}>
          {loading ? 'Calculating...' : 'Calculate'}
        </button>
      </form>
    </FormPanel>
  );
}

function createInitialState(units) {
  const defaultUnit = units[0] || '';

  return {
    thisQuantity: {
      value: '',
      unit: defaultUnit,
    },
    thatQuantity: {
      value: '',
      unit: defaultUnit,
    },
    operation: 'ADD',
    resultUnit: defaultUnit,
  };
}
