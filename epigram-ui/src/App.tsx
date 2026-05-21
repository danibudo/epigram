import { useState, useEffect, useCallback } from 'react'
import type { EpigramDto } from './types'
import EpigramCard from './components/EpigramCard/EpigramCard'
import Controls from './components/Controls/Controls'
import SubmitForm from './components/SubmitForm/SubmitForm'
import styles from './App.module.css'

function App() {
  const [epigram, setEpigram] = useState<EpigramDto | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [autoReload, setAutoReload] = useState(false)
  const [intervalMs, setIntervalMs] = useState(10000)

  const fetchRandom = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const res = await fetch('/api/epigrams/random')
      if (!res.ok) throw new Error(`${res.status}`)
      const data: EpigramDto = await res.json()
      setEpigram(data)
    } catch {
      setError('Could not load epigram. Is the API running?')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchRandom()
  }, [fetchRandom])

  useEffect(() => {
    if (!autoReload) return
    const id = setInterval(fetchRandom, intervalMs)
    return () => clearInterval(id)
  }, [autoReload, intervalMs, fetchRandom])

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
