import { useState } from 'react'
import { createEpigram } from '../../api'
import styles from './SubmitForm.module.css'

type Status = { type: 'success' | 'error'; message: string } | null

export default function SubmitForm() {
  const [text, setText] = useState('')
  const [author, setAuthor] = useState('')
  const [source, setSource] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [status, setStatus] = useState<Status>(null)

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setSubmitting(true)
    setStatus(null)
    try {
      await createEpigram({ text, author: author.trim() || null, source: source.trim() || null })
      setStatus({ type: 'success', message: 'Epigram submitted — thanks for your contribution!' })
      setText('')
      setAuthor('')
      setSource('')
    } catch (err) {
      const status = (err as { status?: number }).status
      if (status === 409) {
        setStatus({ type: 'error', message: 'This epigram already exists.' })
      } else if (status === 400) {
        setStatus({ type: 'error', message: 'Epigram text is required.' })
      } else if (status) {
        setStatus({ type: 'error', message: 'Something went wrong. Please try again.' })
      } else {
        setStatus({ type: 'error', message: 'Network error. Is the API running?' })
      }
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <div className={styles.field}>
        <label className={styles.label} htmlFor="epigram-text">
          Quote <span className={styles.required}>*</span>
        </label>
        <textarea
          id="epigram-text"
          className={styles.textarea}
          value={text}
          onChange={(e) => setText(e.target.value)}
          placeholder="Enter an epigram…"
          rows={3}
          required
        />
      </div>
      <div className={styles.row}>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="epigram-author">
            Author
          </label>
          <input
            id="epigram-author"
            className={styles.input}
            type="text"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
            placeholder="e.g. Maya Angelou"
          />
        </div>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="epigram-source">
            Source
          </label>
          <input
            id="epigram-source"
            className={styles.input}
            type="text"
            value={source}
            onChange={(e) => setSource(e.target.value)}
            placeholder="e.g. I Know Why the Caged Bird Sings"
          />
        </div>
      </div>
      {status && (
        <p className={`${styles.feedback} ${styles[status.type]}`}>{status.message}</p>
      )}
      <button className={styles.submitBtn} type="submit" disabled={submitting || !text.trim()}>
        {submitting ? 'Submitting…' : 'Submit'}
      </button>
    </form>
  )
}
