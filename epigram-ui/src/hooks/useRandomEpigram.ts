import { useState, useEffect, useCallback } from 'react'
import type { EpigramDto } from '../types'

export function useRandomEpigram() {
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

  return { epigram, loading, error, fetchRandom, autoReload, setAutoReload, intervalMs, setIntervalMs }
}
