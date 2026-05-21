import styles from './Controls.module.css'

interface Props {
  onNext: () => void
  loading: boolean
  autoReload: boolean
  onAutoReloadChange: (value: boolean) => void
  intervalMs: number
  onIntervalChange: (ms: number) => void
}

const INTERVALS = [
  { label: '5s', ms: 5000 },
  { label: '10s', ms: 10000 },
  { label: '30s', ms: 30000 },
]

export default function Controls({
  onNext,
  loading,
  autoReload,
  onAutoReloadChange,
  intervalMs,
  onIntervalChange,
}: Props) {
  return (
    <div className={styles.controls}>
      <button className={styles.nextBtn} onClick={onNext} disabled={loading}>
        Next
      </button>
      <span className={styles.separator} aria-hidden="true" />
      <label className={styles.toggle}>
        <input
          type="checkbox"
          checked={autoReload}
          onChange={(e) => onAutoReloadChange(e.target.checked)}
        />
        Auto-reload
      </label>
      <select
        className={styles.select}
        value={intervalMs}
        onChange={(e) => onIntervalChange(Number(e.target.value))}
        disabled={!autoReload}
        aria-label="Auto-reload interval"
      >
        {INTERVALS.map(({ label, ms }) => (
          <option key={ms} value={ms}>
            {label}
          </option>
        ))}
      </select>
    </div>
  )
}
