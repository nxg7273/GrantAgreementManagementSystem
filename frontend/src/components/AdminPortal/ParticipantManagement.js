import React, { useState, useEffect } from 'react';
import { Box, Heading, Table, Thead, Tbody, Tr, Th, Td, Button, Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter, ModalBody, ModalCloseButton, useDisclosure, useToast } from '@chakra-ui/react';
import axios from 'axios';

const ParticipantManagement = () => {
  const [participants, setParticipants] = useState([]);
  const [selectedParticipant, setSelectedParticipant] = useState(null);
  const { isOpen, onOpen, onClose } = useDisclosure();
  const toast = useToast();

  useEffect(() => {
    fetchParticipants();
  }, []);

  const fetchParticipants = async () => {
    try {
      const response = await axios.get('/api/admin/participants');
      setParticipants(response.data);
    } catch (error) {
      console.error('Error fetching participants:', error);
      toast({
        title: 'Error',
        description: 'Failed to fetch participants',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleViewDetails = (participant) => {
    setSelectedParticipant(participant);
    onOpen();
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Participant Management</Heading>
      <Table variant="simple">
        <Thead>
          <Tr>
            <Th>Name</Th>
            <Th>Email</Th>
            <Th>Total Agreements</Th>
            <Th>Actions</Th>
          </Tr>
        </Thead>
        <Tbody>
          {participants.map((participant) => (
            <Tr key={participant.id}>
              <Td>{participant.name}</Td>
              <Td>{participant.email}</Td>
              <Td>{participant.agreements.length}</Td>
              <Td>
                <Button size="sm" colorScheme="blue" onClick={() => handleViewDetails(participant)}>
                  View Details
                </Button>
              </Td>
            </Tr>
          ))}
        </Tbody>
      </Table>

      <Modal isOpen={isOpen} onClose={onClose} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Participant Details</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            {selectedParticipant && (
              <Box>
                <Heading size="md" mb={4}>Personal Information</Heading>
                <p><strong>Name:</strong> {selectedParticipant.name}</p>
                <p><strong>Email:</strong> {selectedParticipant.email}</p>
                <p><strong>Phone:</strong> {selectedParticipant.phone}</p>

                <Heading size="md" mt={6} mb={4}>Agreement History</Heading>
                <Table variant="simple" size="sm">
                  <Thead>
                    <Tr>
                      <Th>Agreement ID</Th>
                      <Th>Grant</Th>
                      <Th>Status</Th>
                      <Th>Date</Th>
                    </Tr>
                  </Thead>
                  <Tbody>
                    {selectedParticipant.agreements.map((agreement) => (
                      <Tr key={agreement.id}>
                        <Td>{agreement.id}</Td>
                        <Td>{agreement.grant.name}</Td>
                        <Td>{agreement.status}</Td>
                        <Td>{new Date(agreement.createdAt).toLocaleDateString()}</Td>
                      </Tr>
                    ))}
                  </Tbody>
                </Table>
              </Box>
            )}
          </ModalBody>
          <ModalFooter>
            <Button colorScheme="blue" mr={3} onClick={onClose}>
              Close
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Box>
  );
};

export default ParticipantManagement;
