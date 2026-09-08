import { Stack } from "expo-router";
import "./globals.css"
import Toast from "react-native-toast-message";

export default function RootLayout() {
  return (
      <>
          <Stack>
              <Stack.Screen
                  name="(tabs)"
                  options={{headerShown: false}}
              />
              <Stack.Screen
                  name="addNewStampLocation"
                  options={{
                      headerShown: false,
                      presentation:'modal'}}/>
          </Stack>
          <Toast/>
      </>
  )
}
