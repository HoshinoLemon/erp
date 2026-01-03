/**
 * 格式化时间
 * @param {String|Number|Date} time - 时间值
 * @return {String} 格式化后的时间字符串（yyyy-MM-dd HH:mm:ss）
 */
export const formatTime = (time) => {
  if (!time) return '';
  
  const date = new Date(time);
  const year = date.getFullYear();
  const month = padZero(date.getMonth() + 1);
  const day = padZero(date.getDate());
  const hours = padZero(date.getHours());
  const minutes = padZero(date.getMinutes());
  const seconds = padZero(date.getSeconds());
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

/**
 * 数字补零
 * @param {Number} num - 需补零的数字
 * @return {String} 补零后的字符串
 */
const padZero = (num) => {
  return num < 10 ? `0${num}` : num.toString();
};