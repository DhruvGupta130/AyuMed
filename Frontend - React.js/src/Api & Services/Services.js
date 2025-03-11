export const generateLabel = (field) =>{
    return field.replace(/([A-Z])/g, ' $1').replace(/^./, (str) => str.toUpperCase());
}
export const formatGender = (gender) => {
    return gender.charAt(0) + gender.substring(1).toLowerCase();
}
export const stringToList = (string) => {
  return string.split(/(?<=\.\s)/) 
    .map(item => item.trim())
    .filter(item => item.length > 0)
    .map(certification => {
      const [heading, ...description] = certification.split(':');
      const descriptionText = description.join(':').trim();
      return { heading, descriptionText };
  });
}

export const getAvatarText = (fullName) => {
  if(!fullName) return;
  const titlesToSkip = ["Dr.", "Mr.", "Mrs.", "Ms."];
  const names = fullName.split(' ').filter(name => !titlesToSkip.includes(name)); // Filter out titles
  const firstLetter = names[0]?.charAt(0).toUpperCase();
  const lastLetter = names[names.length - 1]?.charAt(0).toUpperCase();
  return firstLetter + lastLetter;
};

export const convertTo12HourFormat = (timeString) => {
  if (!timeString) return ""; 
  const [hours, minutes] = timeString.split(":").map(Number);
  const period = hours >= 12 ? "PM" : "AM";
  const formattedHours = hours % 12 || 12;
  return `${formattedHours}:${String(minutes).padStart(2, "0")} ${period}`;
};
