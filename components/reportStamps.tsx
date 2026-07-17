import { Image, Pressable } from 'react-native';
import {router} from "expo-router";
import {colours} from "@/constants/colours";

/* Todo:
- make button actually pressable, maybe with a bit on animation
- logic for adding stamps
*/

const ReportStamp = () => {

    const reportIcon = require("../assets/images/icons/validate-stamp.png");

    return (
        <Pressable
            onPress={() => router.push('/addNewStampLocation')}
            style={{
                alignItems: 'center',
                justifyContent: 'center',
                alignSelf: 'center',
                backgroundColor: colours.primary.background,
                borderRadius: 20,
                width: '100%',
                height: '100%',
                shadowColor: '#000',
                shadowOffset: { width: 0, height: 2 },
                shadowOpacity: 0.18,
                shadowRadius: 6,
                elevation: 5,
            }}>
            <Image source={reportIcon}
                   resizeMode="contain"
                   tintColor={colours.primary.light}
                   style={{
                       width: '60%',
                       height: '60%'}}/>
        </Pressable>
    );
}
export default ReportStamp;