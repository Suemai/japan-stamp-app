export function getImageSource(imageUri: string): { uri: string; headers?: { referer: string } } {
  const normalizedUri = imageUri?.toLowerCase() ?? '';

  if (normalizedUri.includes('funakiya')) {
    return {
      uri: imageUri,
      headers: {
        referer: 'https://stamp.funakiya.com/',
      },
    };
  }

  return {
    uri: imageUri,
  };
}
