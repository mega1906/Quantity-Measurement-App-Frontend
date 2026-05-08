export function AppHeader({ apiBaseUrl }) {
  return (
    <header className="app-header">
      <div>
        <p className="eyebrow">Frontend</p>
        <span className="brand-mark">Quantity Measurement App</span>
      </div>
      <div className="backend-pill">
        <span>Backend</span>
        <strong>{apiBaseUrl}</strong>
      </div>
    </header>
  );
}
