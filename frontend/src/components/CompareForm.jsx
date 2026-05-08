import { useEffect, useState } from 'react';
import { FormPanel } from './FormPanel';
import { QuantityInputRow } from './QuantityInputRow';

export function CompareForm({ measurementType, units, loading, onSubmit }) {
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
      title="Compare quantities"
      subtitle={`Check whether one ${measurementType.toLowerCase()} value is greater, lesser, or equal.`}
    >
      <form className="form-grid" onSubmit={handleSubmit}>
        <QuantityInputRow
          label="First quantity"
          quantity={formState.thisQuantityDto}
          units={units}
          onChange={(field, value) => handleValueChange('thisQuantityDto', field, value)}
        />
        <QuantityInputRow
          label="Second quantity"
          quantity={formState.thatQuantityDto}
          units={units}
          onChange={(field, value) => handleValueChange('thatQuantityDto', field, value)}
        />
        <button className="primary-button" type="submit" disabled={loading}>
          {loading ? 'Comparing...' : 'Compare'}
        </button>
      </form>
    </FormPanel>
  );
}

function createInitialState(units) {
  const defaultUnit = units[0] || '';

  return {
    thisQuantityDto: {
      value: '',
      unit: defaultUnit,
    },
    thatQuantityDto: {
      value: '',
      unit: defaultUnit,
    },
  };
}
