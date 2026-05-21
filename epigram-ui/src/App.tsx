import { useRandomEpigram } from './hooks/useRandomEpigram'
import EpigramCard from './components/EpigramCard/EpigramCard'
import Controls from './components/Controls/Controls'
import SubmitForm from './components/SubmitForm/SubmitForm'
import styles from './App.module.css'

function App() {
  const { epigram, loading, error, fetchRandom, autoReload, setAutoReload, intervalMs, setIntervalMs } =
    useRandomEpigram()

  return (
    <div className={styles.app}>
      <header className={styles.header}>
        <p className={styles.title}>Epigram</p>
      </header>

      <main className={styles.main}>
        <EpigramCard epigram={epigram} loading={loading} error={error} />
        <Controls
          onNext={fetchRandom}
          loading={loading}
          autoReload={autoReload}
          onAutoReloadChange={setAutoReload}
          intervalMs={intervalMs}
          onIntervalChange={setIntervalMs}
        />
      </main>

      <div className={styles.divider} />

      <section className={styles.submitSection}>
        <p className={styles.submitTitle}>Share an Epigram</p>
        <SubmitForm />
      </section>
    </div>
  )
}

export default App
