export function HistoryPanel({ items, loading, deleting, onRefresh, onClear }) {
  return (
    <section className="panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Audit</p>
          <h2>Operation history</h2>
        </div>
        <div className="panel-actions">
          <button className="ghost-button" type="button" onClick={onRefresh} disabled={loading}>
            {loading ? 'Refreshing...' : 'Refresh'}
          </button>
          <button className="danger-button" type="button" onClick={onClear} disabled={deleting}>
            {deleting ? 'Clearing...' : 'Clear all'}
          </button>
        </div>
      </div>

      {items.length === 0 ? (
        <p className="empty-state">No operations saved yet.</p>
      ) : (
        <div className="history-list">
          {items
            .slice()
            .reverse()
            .map((item) => (
              <article key={item.id} className="history-card">
                <div className="history-card-header">
                  <strong>{item.operation}</strong>
                  <span>{formatDate(item.createdAt)}</span>
                </div>
                <p>
                  <span>Input:</span> {formatQuantity(item.thisValue, item.thisUnit)}
                  {item.thatUnit ? ` and ${formatQuantity(item.thatValue, item.thatUnit)}` : ''}
                </p>
                <p>
                  <span>Result:</span>{' '}
                  {item.resultString || formatQuantity(item.resultValue, item.resultUnit)}
                </p>
                {item.errorMessage ? (
                  <p className="history-error">
                    <span>Error:</span> {item.errorMessage}
                  </p>
                ) : null}
              </article>
            ))}
        </div>
      )}
    </section>
  );
}

function formatQuantity(value, unit) {
  if (value === null || value === undefined || !unit) {
    return 'Not available';
  }

  return `${value} ${unit}`;
}

function formatDate(value) {
  if (!value) {
    return 'Unknown time';
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return new Intl.DateTimeFormat('en-IN', {
    dateStyle: 'medium',
    timeStyle: 'short',
  }).format(date);
}
