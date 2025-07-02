#!/bin/bash

# Check if an argument (the .app file) is provided
if [ $# -ne 1 ]; then
    echo "Usage: $0 <path_to_app>"
    exit 1
fi

# Check if the provided .app file exists
app_file="$1"
if [ ! -d "$app_file" ]; then
    echo "The provided .app file does not exist."
    exit 1
fi

# Read the list of simulators from simulators.txt
simulators_file="simulators.txt"
if [ ! -f "$simulators_file" ]; then
    echo "The simulators.txt file does not exist or is empty. Exiting."
    exit 1
fi

# Read the list of simulators and install the app on each one
while IFS= read -r udid
do
    # Check if the simulator with the provided UDID exists
    if xcrun simctl list | grep -q "$udid"; then
        # Check if the simulator is already booted
        if xcrun simctl list | grep -A 3 "$udid" | grep -q "Booted"; then
            echo "Simulator with UDID $udid is already booted. Skipping."
        else
            # Boot the simulator
            xcrun simctl boot "$udid"
            # Install the app on the simulator
            xcrun simctl install "$udid" "$app_file"
        fi
    else
        echo "Simulator with UDID $udid not found. Skipping."
    fi
done < "$simulators_file"

# If no valid simulators were found, use a default iPhone simulator
if [ $? -ne 0 ]; then
    # Get the UDID of the first available iPhone simulator
    iphone_udid=$(xcrun simctl list devices | grep "iPhone" | sed -n '1p' | awk -F '[(|)]' '{print $2}')

    if [ -z "$iphone_udid" ]; then
        echo "No valid iPhone simulator found. Exiting."
        exit 1
    fi

    # Boot the selected iPhone simulator
    xcrun simctl boot "$iphone_udid"
    # Install the app on the iPhone simulator
    xcrun simctl install "$iphone_udid" "$app_file"
fi

# Open the graphical interface of the simulators
open -a "Simulator"

echo "Installation completed on the specified simulators or default iPhone simulator. The Simulators' graphical interface has been opened."
