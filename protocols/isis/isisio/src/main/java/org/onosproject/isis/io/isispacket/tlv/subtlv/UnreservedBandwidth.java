/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.isis.io.isispacket.tlv.subtlv;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Bytes;
import org.jboss.netty.buffer.ChannelBuffer;
import org.onosproject.isis.io.isispacket.tlv.TlvHeader;
import org.onosproject.isis.io.util.IsisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an unreserved band width TE value.
 */
public class UnreservedBandwidth extends TlvHeader implements TrafficEngineeringSubTlv {
    private List<Float> unReservedBandwidth = new ArrayList<>();

    /**
     * Creates an instance of unreserved band width.
     *
     * @param header tlv header instance
     */
    public UnreservedBandwidth(TlvHeader header) {
        this.setTlvType(header.tlvType());
        this.setTlvLength(header.tlvLength());
    }

    /**
     * Adds value of un reserved bandwidth .
     *
     * @param unreservedBandwidth value of un reserved bandwidth
     */
    public void addUnReservedBandwidth(float unreservedBandwidth) {
        this.unReservedBandwidth.add(unreservedBandwidth);
    }

    /**
     * Returns list of un reserved bandwidth .
     *
     * @return List of un reserved bandwidth
     */
    public List<Float> unReservedBandwidthValue() {
        return this.unReservedBandwidth;
    }

    /**
     * Reads bytes from channel buffer .
     *
     * @param channelBuffer channel buffer instance
     */
    public void readFrom(ChannelBuffer channelBuffer) {
        while (channelBuffer.readableBytes() >= IsisUtil.FOUR_BYTES) {
            int maxReversibleBandwidth = channelBuffer.readInt();
            this.addUnReservedBandwidth(Float.intBitsToFloat(maxReversibleBandwidth));
        }
    }

    /**
     * Returns instance as byte array.
     *
     * @return instance as byte array
     */
    public byte[] asBytes() {
        byte[] linkSubType = null;

        byte[] linkSubTlvHeader = tlvHeaderAsByteArray();
        byte[] linkSubTlvBody = tlvBodyAsBytes();
        linkSubType = Bytes.concat(linkSubTlvHeader, linkSubTlvBody);

        return linkSubType;
    }

    /**
     * Returns unreserved bandwidth as byte array.
     *
     * @return unreserved bandwidth as byte array
     */
    public byte[] tlvBodyAsBytes() {
        List<Byte> linkSubTypeBody = new ArrayList<>();
        if (this.unReservedBandwidth.size() < 8) {
            int size = IsisUtil.EIGHT_BYTES - this.unReservedBandwidth.size();
            for (int i = 0; i < size; i++) {
                linkSubTypeBody.addAll(Bytes.asList(IsisUtil.convertToFourBytes(IsisUtil.INITIAL_BANDWIDTH)));
            }
        }
        for (Float unreservedBandwidth : this.unReservedBandwidth) {
            int unresBandwidth = Float.floatToIntBits(unreservedBandwidth);
            linkSubTypeBody.addAll(Bytes.asList(IsisUtil.convertToFourBytes(unresBandwidth)));
        }

        return Bytes.toArray(linkSubTypeBody);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .omitNullValues()
                .add("unReservedBandwidth", unReservedBandwidth)
                .toString();
    }
}