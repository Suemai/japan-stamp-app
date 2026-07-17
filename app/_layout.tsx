import { Stack } from "expo-router";
import "./globals.css"

export default function RootLayout() {
  return <Stack>
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
}
