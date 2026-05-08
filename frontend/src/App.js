import './App.css';
import { useCallback, useEffect, useMemo, useState } from 'react';
import {
  clearHistory,
  compareQuantities,
  convertQuantity,
  convertTemperature,
  deleteHistoryById,
  fetchHistory,
  fetchTemperatureScales,
  fetchUnitsByType,
  runArithmetic,
} from './api/quantityApi';

const TYPE_OPTIONS = [
  { key: 'LENGTH', label: 'Length', icon: '📏' },
  { key: 'WEIGHT', label: 'Weight', icon: '⚖️' },
  { key: 'TEMPERATURE', label: 'Temperature', icon: '🌡️' },
  { key: 'VOLUME', label: 'Volume', icon: '🧪' },
];

const ACTION_OPTIONS = ['Comparison', 'Conversion', 'Arithmetic'];
const HISTORY_OPERATION_OPTIONS = ['ALL', 'CONVERT', 'COMPARE', 'ADD', 'SUBTRACT', 'MULTIPLY', 'DIVIDE'];

const OPERATOR_TO_BACKEND = {
  '+': 'ADD',
  '-': 'SUBTRACT',
  '*': 'MULTIPLY',
  '/': 'DIVIDE',
};

function App() {
  const [unitsByType, setUnitsByType] = useState({});
  const [historyItems, setHistoryItems] = useState([]);
  const [selectedType, setSelectedType] = useState('LENGTH');
  const [selectedAction, setSelectedAction] = useState('Conversion');
  const [operator, setOperator] = useState('+');
  const [fromValue, setFromValue] = useState('1');
  const [toValue, setToValue] = useState('0');
  const [fromUnit, setFromUnit] = useState('');
  const [toUnit, setToUnit] = useState('');
  const [resultValue, setResultValue] = useState('—');
  const [resultUnit, setResultUnit] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [isCalculating, setIsCalculating] = useState(false);
  const [historyBusy, setHistoryBusy] = useState(false);
  const [highlightResult, setHighlightResult] = useState(false);
  const [historyOperationFilter, setHistoryOperationFilter] = useState('ALL');
  const [historyTypeFilter, setHistoryTypeFilter] = useState('ALL');

  const currentUnits = useMemo(() => unitsByType[selectedType] || [], [unitsByType, selectedType]);
  const isConversion = selectedAction === 'Conversion';
  const showOperators = selectedAction === 'Arithmetic';

  const displayHistory = useMemo(
    () =>
      historyItems
        .slice()
        .reverse()
        .map((item) => ({
          id: item.id,
          type: item.thisMeasurementType || item.resultMeasurementType || selectedType,
          action: item.operation || selectedAction,
          expression: buildHistoryExpression(item),
          result: item.resultString || formatNumber(item.resultValue),
          unit: item.resultUnit || '',
          timestamp: item.createdAt,
          isError: item.isError,
          errorMessage: item.errorMessage,
        })),
    [historyItems, selectedAction, selectedType]
  );

  const refreshHistory = useCallback(async () => {
    setHistoryBusy(true);

    try {
      let items = await fetchHistory();

      if (historyOperationFilter !== 'ALL') {
        items = items.filter((item) => item.operation === historyOperationFilter);
      }

      if (historyTypeFilter !== 'ALL') {
        items = items.filter(
          (item) =>
            item.thisMeasurementType === historyTypeFilter ||
            item.resultMeasurementType === historyTypeFilter ||
            item.thatMeasurementType === historyTypeFilter
        );
      }

      setHistoryItems(items);
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setHistoryBusy(false);
    }
  }, [historyOperationFilter, historyTypeFilter]);

  const calculateResult = useCallback(async () => {
    const numericFromValue = Number(fromValue);
    const numericToValue = Number(toValue);

    if (!Number.isFinite(numericFromValue) || !fromUnit || !toUnit) {
      setResultValue('—');
      setResultUnit('');
      return;
    }

    if (!isConversion && !Number.isFinite(numericToValue)) {
      setResultValue('—');
      setResultUnit('');
      return;
    }

    setIsCalculating(true);
    setError('');

    try {
      if (selectedAction === 'Conversion') {
        const convertedValue =
          selectedType === 'TEMPERATURE'
            ? await convertTemperature({
                value: numericFromValue,
                unit: fromUnit,
                targetUnit: toUnit,
              })
            : await convertQuantity({
                value: numericFromValue,
                unit: fromUnit,
                targetUnit: toUnit,
              });

        setToValue(String(convertedValue));
        setResultValue(formatNumber(convertedValue));
        setResultUnit(toUnit);
      }

      if (selectedAction === 'Comparison') {
        const comparison = await compareQuantities({
          thisQuantityDto: {
            value: numericFromValue,
            unit: fromUnit,
          },
          thatQuantityDto: {
            value: numericToValue,
            unit: toUnit,
          },
        });

        setResultValue(
          formatComparisonMessage({
            leftValue: numericFromValue,
            leftUnit: fromUnit,
            rightValue: numericToValue,
            rightUnit: toUnit,
            backendMessage: comparison,
          })
        );
        setResultUnit('');
      }

      if (selectedAction === 'Arithmetic') {
        const arithmeticValue = await runArithmetic({
          thisQuantity: {
            value: numericFromValue,
            unit: fromUnit,
          },
          thatQuantity: {
            value: numericToValue,
            unit: toUnit,
          },
          operation: OPERATOR_TO_BACKEND[operator],
          resultUnit: toUnit,
        });

        setResultValue(formatNumber(arithmeticValue));
        setResultUnit(toUnit);
      }

      pulseResult();
      refreshHistory();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setIsCalculating(false);
    }
  }, [fromUnit, fromValue, isConversion, operator, refreshHistory, selectedAction, selectedType, toUnit, toValue]);

  const loadInitialData = useCallback(async () => {
    setIsLoading(true);
    setError('');

    try {
      const [loadedUnits, temperatureScales, loadedHistory] = await Promise.all([
        Promise.all(TYPE_OPTIONS.map(async (type) => [type.key, await fetchUnitsByType(type.key)])),
        fetchTemperatureScales(),
        fetchHistory(),
      ]);

      const allUnits = Object.fromEntries(loadedUnits);

      if (temperatureScales?.length) {
        allUnits.TEMPERATURE = temperatureScales;
      }

      setUnitsByType(allUnits);

      const initialUnits = allUnits[selectedType] || [];
      setFromUnit(initialUnits[0] || '');
      setToUnit(initialUnits[1] || initialUnits[0] || '');
      setHistoryItems(loadedHistory);
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setIsLoading(false);
    }
  }, [selectedType]);

  useEffect(() => {
    loadInitialData();
  }, [loadInitialData]);

  useEffect(() => {
    if (isLoading) {
      return;
    }

    refreshHistory();
  }, [historyOperationFilter, historyTypeFilter, isLoading, refreshHistory]);

  useEffect(() => {
    if (!currentUnits.length) {
      return;
    }

    setFromUnit((current) => (currentUnits.includes(current) ? current : currentUnits[0]));
    setToUnit((current) =>
      currentUnits.includes(current)
        ? current
        : currentUnits[1] || currentUnits[0]
    );
    setResultValue('—');
    setResultUnit('');
  }, [currentUnits]);

  useEffect(() => {
    if (selectedAction === 'Conversion') {
      setToValue('0');
    } else {
      setToValue((current) => (current === '0' ? '1' : current));
    }

    setResultValue('—');
    setResultUnit('');
  }, [selectedAction]);
  async function handleClearHistory() {
    setHistoryBusy(true);

    try {
      await clearHistory();
      setHistoryItems([]);
    } catch (requestError) {
      setError(getErrorMessage(requestError));
    } finally {
      setHistoryBusy(false);
    }
  }

  async function handleDeleteHistoryItem(id) {
    setHistoryBusy(true);

    try {
      await deleteHistoryById(id);
      await refreshHistory();
    } catch (requestError) {
      setError(getErrorMessage(requestError));
      setHistoryBusy(false);
    }
  }

  function pulseResult() {
    setHighlightResult(true);
    window.setTimeout(() => setHighlightResult(false), 1500);
  }

  function nudgeFromValue(delta) {
    const nextValue = (Number(fromValue) || 0) + delta;
    setFromValue(String(nextValue));
  }

  return (
    <div className="page-shell">
      <header className="hero-banner">
        <div className="container">
          <h1 className="hero-title">Welcome To Quantity Measurement</h1>
        </div>
      </header>

      <main className="container content-wrap">
        {/* {error ? (
          <div id="error-banner" className="error-banner" role="alert">
            {error}
          </div>
        ) : null} */}

        <section className="mb-5">
          <h2 className="section-title">Choose Type</h2>
          <div className="row g-4">
            {TYPE_OPTIONS.map((type) => (
              <div key={type.key} className="col-12 col-sm-6 col-lg-3">
                <TypeCard
                  icon={type.icon}
                  label={type.label}
                  active={selectedType === type.key}
                  onClick={() => setSelectedType(type.key)}
                />
              </div>
            ))}
          </div>
        </section>

        <section className="mb-5">
          <h2 className="section-title">Choose Action</h2>
          <div className="row g-3">
            {ACTION_OPTIONS.map((action) => (
              <div key={action} className="col-12 col-md-4">
                <button
                  type="button"
                  className={`action-pill action-button action-btn ${
                    selectedAction === action ? 'active action-pill-active' : ''
                  }`}
                  onClick={() => setSelectedAction(action)}
                >
                  {action}
                </button>
              </div>
            ))}
          </div>
        </section>

        <section
          id="operator-selector"
          className="operator-row mb-5"
          aria-label="Arithmetic operators"
          style={{ display: showOperators ? 'flex' : 'none' }}
        >
          <div className="operator-select-wrap">
            <select
              id="operator-select"
              className="operator-select"
              aria-label="Select arithmetic operator"
              value={operator}
              onChange={(event) => setOperator(event.target.value)}
            >
              <option value="+">+</option>
              <option value="-">-</option>
              <option value="*">*</option>
              <option value="/">/</option>
            </select>
            <span className="operator-select-arrow">▼</span>
          </div>
        </section>

        <section>
          <div className="row g-4">
            <div className="col-12 col-lg-6">
              <ValuePanel
                title="From"
                value={fromValue}
                onValueChange={setFromValue}
                unit={fromUnit}
                units={currentUnits}
                onUnitChange={setFromUnit}
                showStepper
                onIncrement={() => nudgeFromValue(1)}
                onDecrement={() => nudgeFromValue(-1)}
                loading={isLoading}
              />
            </div>

            <div className="col-12 col-lg-6">
              <ValuePanel
                title="To"
                value={toValue}
                onValueChange={setToValue}
                unit={toUnit}
                units={currentUnits}
                onUnitChange={setToUnit}
                readOnly={isConversion}
                loading={isLoading}
              />
            </div>
          </div>
        </section>

        <section className="execute-section">
          <button
            type="button"
            className="execute-btn"
            onClick={calculateResult}
            disabled={isLoading || isCalculating || !fromUnit || !toUnit}
          >
            {isCalculating ? 'Executing...' : `Execute ${selectedAction}`}
          </button>
        </section>

        <section className="result-section">
          <h2 className="section-title">Result</h2>
          <div id="result-panel" className={`result-panel ${highlightResult ? 'highlight' : ''}`}>
            <div id="result-value" className="result-value">
              {isCalculating ? 'Calculating...' : resultValue}
            </div>
            <div id="result-unit" className="result-unit">
              {resultUnit}
            </div>
          </div>
        </section>

        <section className="history-section">
          <div className="history-header">
            <h2 className="section-title mb-0">History</h2>
            <div className="history-actions">
              <button type="button" className="history-action-btn" onClick={refreshHistory}>
                {historyBusy ? 'Loading...' : 'Refresh'}
              </button>
              <button type="button" className="history-action-btn danger" onClick={handleClearHistory}>
                Clear All
              </button>
            </div>
          </div>
          <div className="history-filters">
            <label className="history-filter">
              <span>Operation</span>
              <select
                value={historyOperationFilter}
                onChange={(event) => setHistoryOperationFilter(event.target.value)}
              >
                {HISTORY_OPERATION_OPTIONS.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
            </label>
            <label className="history-filter">
              <span>Measurement type</span>
              <select
                value={historyTypeFilter}
                onChange={(event) => setHistoryTypeFilter(event.target.value)}
              >
                <option value="ALL">ALL</option>
                {TYPE_OPTIONS.map((type) => (
                  <option key={type.key} value={type.key}>
                    {type.key}
                  </option>
                ))}
              </select>
            </label>
          </div>
          <HistoryList items={displayHistory} onDelete={handleDeleteHistoryItem} />
        </section>
      </main>
    </div>
  );
}

function TypeCard({ icon, label, active, onClick }) {
  return (
    <article
      className={`choice-card type-card ${active ? 'active choice-card-active' : ''}`}
      onClick={onClick}
      onKeyDown={(event) => {
        if (event.key === 'Enter' || event.key === ' ') {
          onClick();
        }
      }}
      role="button"
      tabIndex={0}
    >
      <div className="choice-icon">{icon}</div>
      <h3 className="choice-label">{label}</h3>
    </article>
  );
}

function ValuePanel({
  title,
  value,
  onValueChange,
  unit,
  units,
  onUnitChange,
  showStepper = false,
  onIncrement,
  onDecrement,
  readOnly = false,
  loading = false,
}) {
  return (
    <div className="input-panel">
      <label className="panel-title">{title}</label>
      <div className="display-box">
        {showStepper ? (
          <div className="display-field-wrap">
            <input
              className="display-input"
              type="number"
              inputMode="decimal"
              step="any"
              value={value}
              onChange={(event) => onValueChange(event.target.value)}
              aria-label={`${title} value`}
              disabled={loading}
            />
            <div className="input-stepper" aria-label={`Adjust ${title.toLowerCase()} value`}>
              <button type="button" className="stepper-btn" onClick={onIncrement}>
                +
              </button>
              <button type="button" className="stepper-btn" onClick={onDecrement}>
                -
              </button>
            </div>
          </div>
        ) : (
          <input
            className="display-input"
            type="number"
            inputMode="decimal"
            step="any"
            value={value}
            onChange={(event) => onValueChange(event.target.value)}
            aria-label={`${title} value`}
            readOnly={readOnly}
            disabled={loading}
          />
        )}
      </div>
      <div className="select-box">
        <select
          className="unit-select"
          value={unit}
          onChange={(event) => onUnitChange(event.target.value)}
          aria-label={`Select ${title.toLowerCase()} unit`}
          disabled={loading}
        >
          {units.map((unitOption) => (
            <option key={unitOption} value={unitOption}>
              {formatUnitLabel(unitOption)}
            </option>
          ))}
        </select>
        <span className="select-arrow">▼</span>
      </div>
    </div>
  );
}

function HistoryList({ items, onDelete }) {
  if (!items.length) {
    return <ul id="history-list" className="history-list"><li className="history-item history-empty">No history yet.</li></ul>;
  }

  return (
    <ul id="history-list" className="history-list">
      {items.map((item) => (
        <li key={item.id} className="history-item">
          <div className="history-meta">
            <span className="history-badge">{formatHistoryBadge(item.type)}</span>
            <span className="history-badge history-badge-soft">{item.action}</span>
          </div>
          <div className="history-expression">{item.expression}</div>
          <div className="history-footer">
            <span className="history-result">
              {item.result}
              {item.unit ? ` ${item.unit}` : ''}
            </span>
            <span className="history-time">{formatTimestamp(item.timestamp)}</span>
          </div>
          {item.isError && item.errorMessage ? (
            <div className="history-error">{item.errorMessage}</div>
          ) : null}
          <button
            type="button"
            className="history-delete-btn"
            onClick={() => onDelete(item.id)}
          >
            Delete
          </button>
        </li>
      ))}
    </ul>
  );
}

function buildHistoryExpression(item) {
  const firstValue = formatNumber(item.thisValue);
  const secondValue =
    item.thatValue === null || item.thatValue === undefined
      ? ''
      : ` and ${formatNumber(item.thatValue)} ${item.thatUnit}`;

  return `${item.operation}: ${firstValue} ${item.thisUnit}${secondValue}`;
}

function formatUnitLabel(unit) {
  return unit.replace(/_/g, ' ');
}

function formatHistoryBadge(value) {
  return value ? String(value).replace(/_/g, ' ') : 'Unknown';
}

function formatNumber(value) {
  if (value === null || value === undefined || value === '') {
    return '—';
  }

  const numericValue = Number(value);

  if (!Number.isFinite(numericValue)) {
    return String(value);
  }

  return new Intl.NumberFormat('en-IN', { maximumFractionDigits: 6 }).format(numericValue);
}

function formatTimestamp(timestamp) {
  if (!timestamp) {
    return 'Unknown time';
  }

  const date = new Date(timestamp);

  if (Number.isNaN(date.getTime())) {
    return timestamp;
  }

  return date.toLocaleString();
}

function formatComparisonMessage({
  leftValue,
  leftUnit,
  rightValue,
  rightUnit,
  backendMessage,
}) {
  const normalizedMessage = String(backendMessage || '').toUpperCase();
  const left = `${formatCompactValue(leftValue)} ${leftUnit}`;
  const right = `${formatCompactValue(rightValue)} ${rightUnit}`;

  if (normalizedMessage.includes('GREATER')) {
    return `${left} is greater than ${right}`;
  }

  if (normalizedMessage.includes('LESS')) {
    return `${left} is lesser than ${right}`;
  }

  if (normalizedMessage.includes('EQUAL')) {
    return `${left} is equal to ${right}`;
  }

  return backendMessage;
}

function formatCompactValue(value) {
  if (!Number.isFinite(Number(value))) {
    return String(value);
  }

  return Number(value).toString();
}

function getErrorMessage(error) {
  if (error instanceof Error) {
    return error.message;
  }

  return 'Request failed';
}

export default App;
