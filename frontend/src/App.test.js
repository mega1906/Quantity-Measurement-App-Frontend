import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from './App';
import * as quantityApi from './api/quantityApi';

jest.mock('./api/quantityApi');

const unitMap = {
  LENGTH: ['METER', 'CENTIMETER'],
  WEIGHT: ['KILOGRAM', 'GRAM'],
  TEMPERATURE: ['CELSIUS', 'FAHRENHEIT'],
  VOLUME: ['LITER', 'MILLILITER'],
};

beforeEach(() => {
  jest.clearAllMocks();

  quantityApi.fetchUnitsByType.mockImplementation((measurementType) =>
    Promise.resolve(unitMap[measurementType] || [])
  );
  quantityApi.fetchTemperatureScales.mockResolvedValue(['CELSIUS', 'FAHRENHEIT']);
  quantityApi.fetchHistory.mockResolvedValue([]);
  quantityApi.convertQuantity.mockResolvedValue(100);
  quantityApi.convertTemperature.mockResolvedValue(212);
  quantityApi.compareQuantities.mockResolvedValue('EQUAL');
  quantityApi.runArithmetic.mockResolvedValue(25);
});

test('renders the app and shows the hero heading', async () => {
  render(<App />);

  const heading = await screen.findByText(/welcome to quantity measurement/i);
  expect(heading).toBeInTheDocument();
});

test('performs a length conversion when Conversion is selected', async () => {
  render(<App />);

  await screen.findByText(/welcome to quantity measurement/i);
  await waitFor(() => expect(screen.getByLabelText('Select from unit')).toBeEnabled());

  userEvent.selectOptions(screen.getByLabelText('Select from unit'), ['METER']);
  userEvent.selectOptions(screen.getByLabelText('Select to unit'), ['CENTIMETER']);

  const fromValueInput = screen.getByLabelText('From value');
  await userEvent.clear(fromValueInput);
  await userEvent.type(fromValueInput, '1');

  const executeButton = screen.getByRole('button', { name: /Execute Conversion/i });
  await userEvent.click(executeButton);

  await waitFor(() => expect(quantityApi.convertQuantity).toHaveBeenCalledWith({
    value: 1,
    unit: 'METER',
    targetUnit: 'CENTIMETER',
  }));

  expect(await screen.findByText('100')).toBeInTheDocument();
});

test('performs a temperature conversion when Temperature type is selected', async () => {
  render(<App />);

  await screen.findByText(/welcome to quantity measurement/i);
  await userEvent.click(screen.getByRole('button', { name: /Temperature/i }));

  await waitFor(() => expect(screen.getByLabelText('Select from unit')).toBeEnabled());

  userEvent.selectOptions(screen.getByLabelText('Select from unit'), ['CELSIUS']);
  userEvent.selectOptions(screen.getByLabelText('Select to unit'), ['FAHRENHEIT']);

  const fromValueInput = screen.getByLabelText('From value');
  await userEvent.clear(fromValueInput);
  await userEvent.type(fromValueInput, '100');

  const executeButton = screen.getByRole('button', { name: /Execute Conversion/i });
  await userEvent.click(executeButton);

  await waitFor(() => expect(quantityApi.convertTemperature).toHaveBeenCalledWith({
    value: 100,
    unit: 'CELSIUS',
    targetUnit: 'FAHRENHEIT',
  }));

  expect(await screen.findByText('212')).toBeInTheDocument();
});

test.each([
  ['EQUAL', /1 METER is equal to 100 CENTIMETER/i],
  ['GREATER', /1 METER is greater than 100 CENTIMETER/i],
  ['LESS', /1 METER is lesser than 100 CENTIMETER/i],
])(
  'performs a comparison operation and shows %s comparison result',
  async (backendMessage, expectedMessage) => {
    quantityApi.compareQuantities.mockResolvedValue(backendMessage);

    render(<App />);
    await screen.findByText(/welcome to quantity measurement/i);

    await userEvent.click(screen.getByRole('button', { name: /Comparison/i }));
    await waitFor(() => expect(screen.getByLabelText('Select from unit')).toBeEnabled());

    userEvent.selectOptions(screen.getByLabelText('Select from unit'), ['METER']);
    userEvent.selectOptions(screen.getByLabelText('Select to unit'), ['CENTIMETER']);

    const fromValueInput = screen.getByLabelText('From value');
    const toValueInput = screen.getByLabelText('To value');
    await userEvent.clear(fromValueInput);
    await userEvent.type(fromValueInput, '1');
    await userEvent.clear(toValueInput);
    await userEvent.type(toValueInput, '100');

    const executeButton = screen.getByRole('button', { name: /Execute Comparison/i });
    await userEvent.click(executeButton);

    expect(await screen.findByText(expectedMessage)).toBeInTheDocument();
  }
);

test('performs an arithmetic addition operation', async () => {
  quantityApi.runArithmetic.mockResolvedValue(25);

  render(<App />);
  await screen.findByText(/welcome to quantity measurement/i);
  await userEvent.click(screen.getByRole('button', { name: /Arithmetic/i }));

  await waitFor(() => expect(screen.getByLabelText('Select from unit')).toBeEnabled());

  userEvent.selectOptions(screen.getByLabelText('Select from unit'), ['METER']);
  userEvent.selectOptions(screen.getByLabelText('Select to unit'), ['METER']);

  const fromValueInput = screen.getByLabelText('From value');
  const toValueInput = screen.getByLabelText('To value');
  await userEvent.clear(fromValueInput);
  await userEvent.type(fromValueInput, '10');
  await userEvent.clear(toValueInput);
  await userEvent.type(toValueInput, '15');

  const executeButton = screen.getByRole('button', { name: /Execute Arithmetic/i });
  await userEvent.click(executeButton);

  await waitFor(() => expect(quantityApi.runArithmetic).toHaveBeenCalledWith({
    thisQuantity: { value: 10, unit: 'METER' },
    thatQuantity: { value: 15, unit: 'METER' },
    operation: 'ADD',
    resultUnit: 'METER',
  }));

  expect(await screen.findByText('25')).toBeInTheDocument();
});
