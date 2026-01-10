export function formatAmount(value, options = {}) {
  if (value === null || value === undefined || value === '') return ''
  const n = Number(value)
  if (Number.isNaN(n)) return String(value)
  const { locale = 'fr-FR', minimumFractionDigits = 0, maximumFractionDigits = 2 } = options
  return new Intl.NumberFormat(locale, { minimumFractionDigits, maximumFractionDigits }).format(n)
}
