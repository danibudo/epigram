import type { EpigramDto } from '../../types'
import styles from './EpigramCard.module.css'

interface Props {
  epigram: EpigramDto | null
  loading: boolean
  error: string | null
}

export default function EpigramCard({ epigram, loading, error }: Props) {
  if (loading) {
    return (
      <div className={styles.card}>
        <p className={styles.placeholder}>Loading…</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className={styles.card}>
        <p className={styles.errorText}>{error}</p>
      </div>
    )
  }

  if (!epigram) return null

  const attribution = [epigram.author, epigram.source].filter(Boolean).join(', ')

  return (
    <div className={styles.card}>
      <blockquote className={styles.quote}>
        <p className={styles.text}>{epigram.text}</p>
        {attribution && (
          <footer className={styles.attribution}>
            <cite>— {attribution}</cite>
          </footer>
        )}
      </blockquote>
    </div>
  )
}
