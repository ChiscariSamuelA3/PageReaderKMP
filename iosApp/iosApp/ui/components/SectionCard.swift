//
//  SectionCard.swift
//  iosApp
//
//  Created by Samuel Chiscari on 31/10/2026.
//

import SwiftUI

struct SectionCard<Content: View>: View {
    @ViewBuilder let content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 8) { content }
            .padding(16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color(.white), in: RoundedRectangle(cornerRadius: 16))
    }
}
