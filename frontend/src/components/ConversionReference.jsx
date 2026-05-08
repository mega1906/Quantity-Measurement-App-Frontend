export function ConversionReference({ conversionPairs }) {
  return (
    <section className="panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Reference</p>
          <h2>Supported conversions</h2>
        </div>
      </div>
      <div className="reference-list">
        {conversionPairs.map((pair) => (
          <span key={pair} className="reference-chip">
            {pair}
          </span>
        ))}
      </div>
    </section>
  );
}
