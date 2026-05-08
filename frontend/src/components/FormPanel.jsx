export function FormPanel({ title, subtitle, children }) {
  return (
    <section className="panel">
      <div className="panel-heading">
        <div>
          <p className="eyebrow">Operation</p>
          <h2>{title}</h2>
        </div>
      </div>
      <p className="panel-copy">{subtitle}</p>
      {children}
    </section>
  );
}
