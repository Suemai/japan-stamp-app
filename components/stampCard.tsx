import {View, Text, TouchableOpacity, Image} from 'react-native'
import React from 'react'
import {Link} from "expo-router";

/* The page that shows the stamp card for my stamps and all stamps
TODO:
- Make it look nice!

- This is basically your bit partner!
- Fetch data from database to display it here
-> will be temp data for now

 */

type StampCardProps = {
    id: Number
    name: String
    imageUri: String
}

const StampCard = ({id, name, imageUri}: StampCardProps) => {
    return (
        <Link href={`/stamps/${id}`} asChild>
            <TouchableOpacity
                className= "w-[30%] rounded-xl bg-white p-1"
            style={{
                shadowColor: "#000",
                shadowOffset: { width: 0, height: 2 },
                shadowOpacity: 0.15,
                shadowRadius: 4,
                elevation: 5,
            }}>
                <Image
                    source={{uri: imageUri}}
                    className= "w-full rounded-lg"
                    style={{aspectRatio: 1}}
                    resizeMode="cover"
                />
                <Text className="text-sm font-bold mt-2">{name}</Text>

            </TouchableOpacity>
        </Link>
    )
}
export default StampCard