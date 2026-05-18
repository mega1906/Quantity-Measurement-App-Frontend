const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://32.236.14.140:8081';

export async function fetchUnitsByType(measurementType) {
  return request(`/units/type/${measurementType}`);
}

export async function fetchTemperatureScales() {
  return request('/temperature/scales');
}

export async function fetchAllConversions() {
  return request('/conversions/all');
}

export async function fetchHistory() {
  return request('/history');
}

export async function fetchHistoryByOperation(operation) {
  return request(`/history/operation/${encodeURIComponent(operation)}`);
}

export async function fetchHistoryByMeasurementType(measurementType) {
  return request(`/history/type/${encodeURIComponent(measurementType)}`);
}

export async function clearHistory() {
  return request('/history', {
    method: 'DELETE',
  });
}

export async function deleteHistoryById(id) {
  return request(`/history/${id}`, {
    method: 'DELETE',
  });
}

export async function convertQuantity({ value, unit, targetUnit }) {
  return request(`/api/v1/quantities/convert?targetUnit=${encodeURIComponent(targetUnit)}`, {
    method: 'POST',
    body: JSON.stringify({
      value: Number(value),
      unit,
    }),
  });
}

export async function runArithmetic({ thisQuantity, thatQuantity, operation, resultUnit }) {
  return request('/api/v1/quantities/arithmetic', {
    method: 'POST',
    body: JSON.stringify({
      thisQuantity: {
        value: Number(thisQuantity.value),
        unit: thisQuantity.unit,
      },
      thatQuantity: {
        value: Number(thatQuantity.value),
        unit: thatQuantity.unit,
      },
      operation,
      resultUnit,
    }),
  });
}

export async function compareQuantities({ thisQuantityDto, thatQuantityDto }) {
  return request('/api/v1/quantities/compare', {
    method: 'POST',
    body: JSON.stringify({
      thisQuantityDto: {
        value: Number(thisQuantityDto.value),
        unit: thisQuantityDto.unit,
      },
      thatQuantityDto: {
        value: Number(thatQuantityDto.value),
        unit: thatQuantityDto.unit,
      },
    }),
  });
}

export async function convertTemperature({ value, unit, targetUnit }) {
  return request(`/temperature/convert?targetUnit=${encodeURIComponent(targetUnit)}`, {
    method: 'POST',
    body: JSON.stringify({
      value: Number(value),
      unit,
    }),
  });
}

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('content-type') || '';

  if (contentType.includes('application/json')) {
    return response.json();
  }

  return response.text();
}
