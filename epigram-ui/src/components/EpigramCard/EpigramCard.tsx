import type { EpigramDto } from '../../types'
import styles from './EpigramCard.module.css'

interface Props {
  epigram: EpigramDto | null
  loading: boolean
  error: string | null
}

export default function EpigramCard({ epigram, loading, error }: Props) {
  const attribution = epigram
    ? [epigram.author, epigram.source].filter(Boolean).join(', ')
    : ''

  return (
    <div className={styles.card}>
      {loading ? (
        <p className={styles.placeholder}>Loading…</p>
      ) : error ? (
        <p className={styles.errorText}>{error}</p>
      ) : epigram ? (
        <blockquote className={styles.quote}>
          <p className={styles.text}>{epigram.text}</p>
          {attribution && (
            <footer className={styles.attribution}>
              <cite>— {attribution}</cite>
            </footer>
          )}
        </blockquote>
      ) : null}
    </div>
  )
}
