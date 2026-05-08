export function ResultBanner({ title, value, detail, tone = 'success' }) {
  return (
    <section className={`result-banner ${tone}`}>
      <div>
        <p className="eyebrow">{title}</p>
        <h2>{value}</h2>
      </div>
      {detail ? <p className="result-detail">{detail}</p> : null}
    </section>
  );
}
