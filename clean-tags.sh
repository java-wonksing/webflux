#!/bin/bash

if [ $# -lt 1 ]; then
    echo "Usage   : ./clean-tags.sh {VERSION_TO_KEEP}"
    echo "Example : ./clean-tags.sh v4.1.10"
    exit 1
fi

VERSION_TO_KEEP=$1

# Function to delete tags locally and remotely
delete_tags() {
    local pattern=$1
    local version_to_keep=$2

    # Get the list of tags to delete
    tags_to_delete=$(git tag -l "${pattern}")

    if [ -n "$tags_to_delete" ]; then
        # Convert tags to array to handle spaces and special characters properly
        IFS=$'\n' read -r -d '' -a tags_array <<< "$tags_to_delete"

        # Delete the tags locally
        echo "Deleting local tags:"
        for tag in "${tags_array[@]}"; do
            if ! echo "$tag" | grep -q "^${version_to_keep}"; then
                echo "Deleting tag: $tag"
                git tag -d "$tag"
                git push origin --delete "$tag"
            fi
        done
    else
        echo "No tags to delete for pattern '${pattern}' keeping '${version_to_keep}*'"
    fi
}

# Delete tags with suffixes while keeping those with the specified prefix
delete_tags "*-dev" "$VERSION_TO_KEEP"
delete_tags "*-stg" "$VERSION_TO_KEEP"
delete_tags "*-stg4" "$VERSION_TO_KEEP"
